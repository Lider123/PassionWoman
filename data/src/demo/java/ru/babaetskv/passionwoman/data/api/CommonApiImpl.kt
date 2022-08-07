package ru.babaetskv.passionwoman.data.api

import android.content.Context
import com.squareup.moshi.Moshi
import kotlinx.coroutines.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import ru.babaetskv.passionwoman.data.filters.Filters
import ru.babaetskv.passionwoman.data.database.PassionWomanDatabase
import ru.babaetskv.passionwoman.data.database.entity.transformations.ProductTransformableParamsProvider
import ru.babaetskv.passionwoman.data.filters.FilterResolver
import ru.babaetskv.passionwoman.data.model.*
import ru.babaetskv.passionwoman.domain.model.Sorting
import ru.babaetskv.passionwoman.domain.utils.transformList
import java.util.*

class CommonApiImpl(
    context: Context,
    private val database: PassionWomanDatabase,
    moshi: Moshi,
) : BaseApiImpl(context, moshi), CommonApi {
    private val popularProductsCache = mutableListOf<ProductModel>()
    private val newProductsCache = mutableListOf<ProductModel>()
    private val saleProductsCache = mutableListOf<ProductModel>()
    private val productTransformableParamsProvider =
        ProductTransformableParamsProvider(database, this)

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
        return@processRequest loadListFromAsset(AssetFile.STORIES)
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
                    it.filterModel.toJson(database)
                        .let(::add)
                }
            }.selectAvailableFilters(products)
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
            throw getBadRequestException("Failed to process filters")
        } catch (e: Exception) {
            e.printStackTrace()
            throw getInternalServerErrorException("Internal server error")
        }
    }

    override suspend fun getProductsByIds(ids: String): List<ProductModel> = processRequest {
        val favoriteIds = ids.split(",").map(String::toInt).toSet()
        return@processRequest database.productDao.getByIds(favoriteIds)
            .transformList(productTransformableParamsProvider)
    }

    override suspend fun getPopularBrands(count: Int): List<BrandModel> =
        processRequest {
            return@processRequest database.brandDao.getPopular(count)
                .transformList()
        }

    override suspend fun getProduct(productId: Int): ProductModel = processRequest {
        return@processRequest database.productDao.getById(productId)
            ?.transform(productTransformableParamsProvider)
            ?: throw getNotFoundException("Product not found")
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
    }
}
