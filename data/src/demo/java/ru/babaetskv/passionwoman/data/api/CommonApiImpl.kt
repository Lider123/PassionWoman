package ru.babaetskv.passionwoman.data.api

import android.content.res.AssetManager
import com.squareup.moshi.Moshi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import ru.babaetskv.passionwoman.data.Filters
import ru.babaetskv.passionwoman.data.model.*
import ru.babaetskv.passionwoman.domain.model.Sorting
import java.util.*

class CommonApiImpl(
    private val moshi: Moshi,
    private val assetManager: AssetManager
) : BaseApiImpl(), CommonApi {
    private val popularProductsCache = mutableListOf<ProductModel>()
    private val newProductsCache = mutableListOf<ProductModel>()
    private val saleProductsCache = mutableListOf<ProductModel>()

    override suspend fun authorize(body: AccessTokenModel): AuthTokenModel =
        withContext(Dispatchers.IO) {
            delay(DELAY_LOADING)
            return@withContext AuthTokenModel(TOKEN)
        }

    override suspend fun getCategories(): List<CategoryModel> = withContext(Dispatchers.IO) {
        delay(DELAY_LOADING)
        return@withContext loadListFromAsset(assetManager, AssetFile.CATEGORIES, moshi)
    }

    override suspend fun getPromotions(): List<PromotionModel> = withContext(Dispatchers.IO) {
        delay(DELAY_LOADING)
        return@withContext loadListFromAsset(assetManager, AssetFile.PROMOTIONS, moshi)
    }

    override suspend fun getStories(): List<StoryModel> = withContext(Dispatchers.IO) {
        delay(DELAY_LOADING)
        return@withContext loadListFromAsset(assetManager, AssetFile.STORIES, moshi)
    }

    override suspend fun getProducts(
        categoryId: String?,
        query: String,
        filters: String,
        sorting: String,
        limit: Int,
        offset: Int
    ): ProductsPagedResponseModel = withContext(Dispatchers.IO) {
        try {
            delay(DELAY_LOADING)
            val filtersObject = Filters(JSONArray(filters))
            val sortingObject = Sorting.findValueByApiName(sorting)
            var products: List<ProductModel> = if (categoryId != null) {
                getCategoryProducts(CategoryProducts.findByCategoryId(categoryId)!!)
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
                addAll(loadArrayOfJsonFromAsset(assetManager, AssetFile.FILTERS_COMMON))
            }.selectAvailableFilters(products)
            val pagingIndices = IntRange(offset, offset + limit - 1)
            return@withContext products.let { result ->
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

    override suspend fun getProductsByIds(ids: String): List<ProductModel> {
        delay(DELAY_LOADING)
        val favoriteIds = ids.split(",").toSet()
        return CategoryProducts.values()
            .flatMap<CategoryProducts, ProductModel> {
                loadListFromAsset(assetManager, it.assetFile, moshi)
            }
            .filter { favoriteIds.contains(it.id) }
    }

    override suspend fun getPopularBrands(count: Int): List<BrandModel> =
        withContext(Dispatchers.IO) {
            delay(DELAY_LOADING)
            return@withContext loadListFromAsset<BrandModel>(assetManager, AssetFile.BRANDS, moshi)
                .take(count)
        }

    override suspend fun getProduct(productId: String): ProductModel = withContext(Dispatchers.IO) {
        delay(DELAY_LOADING)
        return@withContext CategoryProducts.values()
            .flatMap<CategoryProducts, ProductModel> {
                loadListFromAsset(assetManager, it.assetFile, moshi)
            }
            .find { it.id == productId } ?: throw getNotFoundException("Product not found")
    }

    private fun getCategoryProducts(category: CategoryProducts): List<ProductModel> =
        loadListFromAsset(assetManager, category.assetFile, moshi)

    private fun getSaleProducts(): List<ProductModel> = saleProductsCache.ifEmpty {
        getAllProducts(null).let {
            Filters.discountOnlyFilters.applyToProducts(it)
        }.also {
            saleProductsCache.addAll(it)
        }
    }

    private fun getPopularProducts() = getAllProducts(popularProductsCache)

    private fun getNewProducts() = getAllProducts(newProductsCache)

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

    private fun getAllProducts(cache: MutableList<ProductModel>?): List<ProductModel> =
        if (cache?.isNotEmpty() == true) {
            cache
        } else {
            CategoryProducts.values().asList()
                .flatMap { loadListFromAsset<ProductModel>(assetManager, it.assetFile, moshi) }
                .shuffled()
                .also {
                    cache?.addAll(it)
                }
        }

    private enum class CategoryProducts(
        val categoryId: String,
        val assetFile: AssetFile
    ) {
        BRA("category_bra", AssetFile.PRODUCTS_BRA),
        PANTIES("category_panties", AssetFile.PRODUCTS_PANTIES),
        LINGERIE("category_lingerie", AssetFile.PRODUCTS_LINGERIE),
        EROTIC("category_erotic", AssetFile.PRODUCTS_EROTIC),
        SWIM("category_swim", AssetFile.PRODUCTS_SWIM),
        CORSET("category_corsets", AssetFile.PRODUCTS_CORSET),
        GARTERBELT("category_garter_belts", AssetFile.PRODUCTS_GARTER_BELTS),
        BABYDOLL("category_babydolls", AssetFile.PRODUCTS_BABYDOLL),
        STOCKINGS("category_stockings", AssetFile.PRODUCTS_STOCKINGS),
        PANTYHOSE("category_pantyhose", AssetFile.PRODUCTS_PANTYHOSE);

        companion object {

            fun findByCategoryId(id: String) = values().find { it.categoryId == id }
        }
    }

    companion object {
        private const val TOKEN = "token"
    }
}
