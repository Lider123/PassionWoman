package ru.babaetskv.passionwoman.data.api

import com.squareup.moshi.JsonDataException
import kotlinx.coroutines.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import ru.babaetskv.passionwoman.data.AssetProvider
import ru.babaetskv.passionwoman.data.filters.Filters
import ru.babaetskv.passionwoman.data.database.PassionWomanDatabase
import ru.babaetskv.passionwoman.data.database.entity.ProductEntity
import ru.babaetskv.passionwoman.data.filters.FilterResolver
import ru.babaetskv.passionwoman.data.model.*
import ru.babaetskv.passionwoman.domain.model.Sorting
import ru.babaetskv.passionwoman.domain.model.base.Transformable.Companion.transformList
import java.util.*

class CommonApiImpl(
    private val database: PassionWomanDatabase,
    private val assetProvider: AssetProvider,
    private val productTransformableParamsProvider: ProductEntity.TransformableParamsProvider,
) : BaseApiImpl(), CommonApi {

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
            val stories = assetProvider.loadListFromAsset<StoryModel>(AssetProvider.AssetFile.STORIES)
            if (stories.any { it.contents.isEmpty() }) {
                throw ApiExceptionProvider.getInternalServerErrorException("Stories without content are not allowed")
            }

            stories
        } catch (e: JsonDataException) {
            throw ApiExceptionProvider.getInternalServerErrorException("Stories source is corrupted")
        }
    }

    override suspend fun getProducts(
        categoryId: Long?,
        query: String,
        filters: String,
        sorting: String,
        limit: Int,
        offset: Int
    ): ProductsPagedResponseModel = processRequest {
        try {
            val filtersObject = Filters(JSONArray(filters))
            val sortingObject = Sorting.findValueByApiName(sorting)
            var products: List<ProductModel> = when {
                categoryId != null -> getCategoryProducts(categoryId)
                filtersObject.isDiscountOnly -> getSaleProducts()
                else -> getAllProducts()
            }
            products = filtersObject.applyToProducts(products)
            products = applyQueryToProducts(query, products)
            products = sortingObject.applyToProducts(products)
            val availableFilters = mutableListOf<JSONObject>().apply {
                FilterResolver.values().forEach {
                    it.getFilterExtractor.invoke()
                        .extractAsJson(database)
                        .let(::add)
                }
            }.selectAvailableFilters(products)
            val pagingIndices = IntRange(offset, offset + limit - 1)
            return@processRequest products
                .slice(products.indices.intersect(pagingIndices))
                .let {
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

        val productIds: Set<Long> = ids.split(",").map(String::toLong).toSet()
        val productEntities = database.productDao.getByIds(productIds.toList())
        val products = productEntities.transformList(productTransformableParamsProvider)
        val allProductsFound = products.map(ProductModel::id)
            .toSet()
            .containsAll(productIds)
        if (!allProductsFound) {
            throw ApiExceptionProvider.getNotFoundException("One or more products with specified ids are not found")
        }

        return@processRequest products
    }

    override suspend fun getPopularBrands(count: Int): List<BrandModel> = processRequest {
        return@processRequest database.brandDao.getPopular(count)
            .transformList()
    }

    override suspend fun getProduct(productId: Long): ProductModel = processRequest {
        return@processRequest database.productDao.getById(productId)
            ?.transform(productTransformableParamsProvider)
            ?: throw ApiExceptionProvider.getNotFoundException("Product not found")
    }

    private fun applyQueryToProducts(query: String, products: List<ProductModel>): List<ProductModel> {
        if (query.isBlank()) return products

        val queryParts = query.lowercase(Locale.getDefault()).split(" ")
        return products.filter { product ->
            val nameParts = product.name.lowercase(Locale.getDefault()).split(" ")
            nameParts.any { namePart ->
                queryParts.any { queryPart ->
                    namePart.startsWith(queryPart)
                }
            }
        }
    }

    private fun Sorting.applyToProducts(products: List<ProductModel>): List<ProductModel> =
        when (this) {
            Sorting.PRICE_ASC -> products.sortedBy(ProductModel::priceWithDiscount)
            Sorting.PRICE_DESC -> products.sortedByDescending(ProductModel::priceWithDiscount)
            Sorting.RATING -> products.sortedByDescending(ProductModel::rating)
            Sorting.NEW -> products.sortedByDescending(ProductModel::createdAt)
            Sorting.POPULARITY -> products
        }

    private suspend fun getCategoryProducts(categoryId: Long): List<ProductModel> =
        database.productDao.getByCategoryId(categoryId)
            .transformList(productTransformableParamsProvider)

    private suspend fun getSaleProducts(): List<ProductModel> =
        database.productDao.getWithDiscount()
            .transformList(productTransformableParamsProvider)

    private suspend fun getAllProducts(): List<ProductModel> =
        database.productDao.getAll()
            .transformList(productTransformableParamsProvider)

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
        private val REGEX_IDS_LIST = "^(\\d+,)*\\d+$".toRegex()
    }
}
