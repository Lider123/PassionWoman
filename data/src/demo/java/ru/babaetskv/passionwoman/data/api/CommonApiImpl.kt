package ru.babaetskv.passionwoman.data.api

import android.content.res.AssetManager
import android.util.Log
import com.squareup.moshi.JsonDataException
import com.squareup.moshi.Moshi
import kotlinx.coroutines.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import ru.babaetskv.passionwoman.data.filters.Filters
import ru.babaetskv.passionwoman.data.database.PassionWomanDatabase
import ru.babaetskv.passionwoman.data.database.entity.ProductEntity
import ru.babaetskv.passionwoman.data.filters.FilterResolver
import ru.babaetskv.passionwoman.data.model.*
import ru.babaetskv.passionwoman.domain.model.Sorting
import ru.babaetskv.passionwoman.domain.model.base.Transformable.Companion.transformList
import java.util.*

class CommonApiImpl(
    assetManager: AssetManager,
    private val database: PassionWomanDatabase,
    private val productTransformableParamsProvider: ProductEntity.TransformableParamsProvider,
    moshi: Moshi,
) : BaseApiImpl(assetManager, moshi), CommonApi {
    private val popularProductsCache = mutableListOf<ProductModel>()
    private val newProductsCache = mutableListOf<ProductModel>()
    private val saleProductsCache = mutableListOf<ProductModel>()

    override suspend fun authorize(body: AccessTokenModel): AuthTokenModel = processRequest {
        return@processRequest AuthTokenModel(TOKEN)
    }

    override suspend fun getCategories(): List<CategoryModel> = processRequest {
        return@processRequest database.categoryDao.getAll()
            .transformList()
    }

    override suspend fun getPromotions(): List<PromotionModel> = processRequest {
        return@processRequest database.promotionDao.getAll()
            .transformList()
    }

    override suspend fun getStories(): List<StoryModel> = processRequest {
        return@processRequest try {
            val stories = loadListFromAsset<StoryModel>(AssetFile.STORIES)
            if (stories.any { it.contents.isEmpty() }) {
                throw ApiExceptionProvider.getInternalServerErrorException("Stories without content are not allowed")
            }

            stories
        } catch (e: JsonDataException) {
            throw ApiExceptionProvider.getInternalServerErrorException("Stories source is corrupted")
        }
    }

    override suspend fun getProducts(
        categoryId: Int?,
        query: String,
        filters: String,
        sorting: String,
        limit: Int,
        offset: Int
    ): ProductsPagedResponseModel = processRequest {
        try {
            val filtersObject = Filters(JSONArray(filters))
            val sortingObject = Sorting.findValueByApiName(sorting)
            var products: List<ProductModel> = if (categoryId != null) {
                getCategoryProducts(categoryId)
            } else when {
                filtersObject.isDiscountOnly -> getSaleProducts()
                sortingObject == Sorting.POPULARITY -> getPopularProducts()
                sortingObject == Sorting.NEW -> getNewProducts()
                else -> getPopularProducts()
            }
            products = filtersObject.applyToProducts(products)
            if (query.isNotBlank()) {
                val queryParts = query.lowercase(Locale.getDefault()).split(" ")
                products = products.filter { product ->
                    val nameParts = product.name.lowercase(Locale.getDefault()).split(" ")
                    nameParts.any { namePart ->
                        queryParts.any { queryPart ->
                            namePart.startsWith(queryPart)
                        }
                    }
                }
            }
            val availableFilters = mutableListOf<JSONObject>().apply {
                FilterResolver.values().forEach {
                    it.getFilterExtractor.invoke()
                        .extractAsJson(database)
                        .let(::add)
                }
            }.also {
                Log.e(CommonApiImpl::class.simpleName, "All filters: $it") // TODO: remove
            }.selectAvailableFilters(products).also {
                Log.e(CommonApiImpl::class.simpleName, "Available filters: $it") // TODO: remove
            }
            val pagingIndices = IntRange(offset, offset + limit - 1)
            return@processRequest products.let { result ->
                when (sortingObject) {
                    Sorting.PRICE_ASC -> result.sortedBy { it.priceWithDiscount }
                    Sorting.PRICE_DESC -> result.sortedByDescending { it.priceWithDiscount }
                    else -> result
                }.slice(products.indices.intersect(pagingIndices))
            }.let {
                ProductsPagedResponseModel(
                    products = it,
                    total = products.size,
                    availableFilters = availableFilters
                )
            }
        } catch (e: JSONException) {
            e.printStackTrace()
            throw ApiExceptionProvider.getBadRequestException("Failed to process filters")
        } catch (e: Exception) {
            e.printStackTrace()
            throw ApiExceptionProvider.getInternalServerErrorException("Internal server error")
        }
    }

    override suspend fun getProductsByIds(ids: String): List<ProductModel> = processRequest {
        if (ids.isBlank()) return@processRequest emptyList()

        if (ids.matches(REGEX_IDS_LIST).not()) {
            throw ApiExceptionProvider.getBadRequestException("Wrong ids list formatting")
        }

        val productIds = ids.split(",").map(String::toInt).toSet()
        val productEntities = database.productDao.getByIds(productIds)
        val products = productEntities.transformList(productTransformableParamsProvider)
        val allProductsFound = products.map(ProductModel::id)
            .toSet()
            .containsAll(productIds)
        if (!allProductsFound) {
            throw ApiExceptionProvider.getNotFoundException("One or more products with specified ids are not found")
        }

        return@processRequest products
    }

    override suspend fun getPopularBrands(count: Int): List<BrandModel> =
        processRequest {
            return@processRequest database.brandDao.getPopular(count)
                .transformList()
        }

    override suspend fun getProduct(productId: Int): ProductModel = processRequest {
        return@processRequest database.productDao.getById(productId)
            ?.transform(productTransformableParamsProvider)
            ?: throw ApiExceptionProvider.getNotFoundException("Product not found")
    }

    private suspend fun getCategoryProducts(categoryId: Int): List<ProductModel> =
        database.productDao.getByCategoryId(categoryId)
            .transformList(productTransformableParamsProvider)

    private suspend fun getSaleProducts(): List<ProductModel> = saleProductsCache.ifEmpty {
        database.productDao.getWithDiscount()
            .transformList(productTransformableParamsProvider)
            .also {
                saleProductsCache.addAll(it)
            }
    }

    private suspend fun getPopularProducts() = popularProductsCache.ifEmpty {
        database.productDao.getRandom(PRODUCT_CACHE_SIZE)
            .transformList(productTransformableParamsProvider)
            .also {
                popularProductsCache.addAll(it)
            }
    }

    private suspend fun getNewProducts() = newProductsCache.ifEmpty {
        database.productDao.getRandom(PRODUCT_CACHE_SIZE)
            .transformList(productTransformableParamsProvider)
            .also {
                newProductsCache.addAll(it)
            }
    }

    private fun List<JSONObject>.selectAvailableFilters(products: List<ProductModel>): List<JSONObject> {
        val array =  JSONArray().apply {
            this@selectAvailableFilters.forEach {
                put(it)
            }
        }
        val availableArray = Filters(array).selectAvailable(products)
        return mutableListOf<JSONObject>().apply {
            for (i in 0 until availableArray.length()) add(availableArray.getJSONObject(i))
        }
    }

    companion object {
        private const val TOKEN = "token"
        private const val PRODUCT_CACHE_SIZE = 10
        private val REGEX_IDS_LIST = "^(\\d+,)*\\d+$".toRegex()
    }
}
