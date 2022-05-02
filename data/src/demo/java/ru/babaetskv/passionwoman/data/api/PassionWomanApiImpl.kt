package ru.babaetskv.passionwoman.data.api

import android.content.res.AssetManager
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.ResponseBody.Companion.toResponseBody
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import retrofit2.HttpException
import retrofit2.Response
import ru.babaetskv.passionwoman.data.Filters
import ru.babaetskv.passionwoman.data.model.*
import ru.babaetskv.passionwoman.domain.HttpCodes.BAD_REQUEST
import ru.babaetskv.passionwoman.domain.HttpCodes.INTERNAL_SERVER_ERROR
import ru.babaetskv.passionwoman.domain.HttpCodes.NOT_FOUND
import ru.babaetskv.passionwoman.domain.model.Sorting
import java.util.*

class PassionWomanApiImpl(
    private val moshi: Moshi,
    private val assetManager: AssetManager
) : PassionWomanApi {
    private var profileMock: ProfileModel? = null
    private var favoriteIdsMock: List<String>? = null
    private val popularProductsCache = mutableListOf<ProductModel>()
    private val newProductsCache = mutableListOf<ProductModel>()
    private val saleProductsCache = mutableListOf<ProductModel>()

    override suspend fun getCategories(): List<CategoryModel> = withContext(Dispatchers.IO) {
        delay(DELAY_LOADING)
        return@withContext loadListFromAsset("categories.json")
    }

    override suspend fun getPromotions(): List<PromotionModel> = withContext(Dispatchers.IO) {
        delay(DELAY_LOADING)
        return@withContext loadListFromAsset("promotions.json")
    }

    override suspend fun getStories(): List<StoryModel> = withContext(Dispatchers.IO) {
        delay(DELAY_LOADING)
        return@withContext loadListFromAsset("stories.json")
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
                addAll(loadArrayOfJsonFromAsset("filters_common.json"))
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

    override suspend fun getPopularBrands(count: Int): List<BrandModel> = withContext(Dispatchers.IO) {
        delay(DELAY_LOADING)
        return@withContext loadListFromAsset<BrandModel>("brands.json").take(count)
    }

    override suspend fun getFavorites(ids: String): List<ProductModel> {
        delay(DELAY_LOADING)
        val favoriteIds = ids.split(",").toSet()
        return CategoryProducts.values()
            .flatMap<CategoryProducts, ProductModel> { loadListFromAsset(it.productsFileName) }
            .filter { favoriteIds.contains(it.id) }
    }

    override suspend fun getProduct(productId: String): ProductModel = withContext(Dispatchers.IO) {
        delay(DELAY_LOADING)
        return@withContext CategoryProducts.values()
            .flatMap<CategoryProducts, ProductModel> { loadListFromAsset(it.productsFileName) }
            .find { it.id == productId } ?: throw getNotFoundException("Product not found")
    }

    override suspend fun getProfile(): ProfileModel = withContext(Dispatchers.IO) {
        delay(DELAY_LOADING)
        return@withContext if (profileMock == null) {
            loadObjectFromAsset<ProfileModel>("profile.json").also { profileMock = it }
        } else profileMock!!
    }

    override suspend fun updateProfile(body: ProfileModel) = withContext(Dispatchers.IO) {
        delay(DELAY_LOADING)
        profileMock = body
    }

    override suspend fun uploadAvatar(image: MultipartBody.Part) = withContext(Dispatchers.IO) {
        delay(DELAY_LOADING)
        // TODO: think up how to save image
    }

    override suspend fun getFavoriteIds(): List<String> = withContext(Dispatchers.IO) {
        delay(DELAY_LOADING)
        return@withContext if (favoriteIdsMock == null) {
            loadListFromAsset<String>("favoriteIds.json").also { favoriteIdsMock = it }
        } else favoriteIdsMock!!
    }

    override suspend fun setFavoriteIds(ids: List<String>) = withContext(Dispatchers.IO) {
        delay(DELAY_LOADING)
        favoriteIdsMock = ids
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

    private fun getCategoryProducts(category: CategoryProducts): List<ProductModel> =
        loadListFromAsset(category.productsFileName)

    private fun getAllProducts(cache: MutableList<ProductModel>?): List<ProductModel> =
        if (cache?.isNotEmpty() == true) {
            cache
        } else {
            CategoryProducts.values().asList()
                .flatMap { loadListFromAsset<ProductModel>(it.productsFileName) }
                .shuffled()
                .also {
                    cache?.addAll(it)
                }
        }

    private fun getSaleProducts(): List<ProductModel> = saleProductsCache.ifEmpty {
        getAllProducts(null).let {
            Filters.discountOnlyFilters.applyToProducts(it)
        }.also {
            saleProductsCache.addAll(it)
        }
    }

    private fun getPopularProducts() = getAllProducts(popularProductsCache)

    private fun getNewProducts() = getAllProducts(newProductsCache)

    private inline fun <reified T> loadObjectFromAsset(filename: String): T {
        val json = assetManager.open(filename).bufferedReader().use{ it.readText()}
        val adapter: JsonAdapter<T> = moshi.adapter(T::class.java)
        return adapter.fromJson(json)!!
    }

    private fun loadArrayOfJsonFromAsset(fileName: String): List<JSONObject> {
        val json = assetManager.open(fileName).bufferedReader().use { it.readText() }
        return JSONArray(json).let {
            val values = mutableListOf<JSONObject>()
            for (i in 0 until it.length()) {
                values.add(it.getJSONObject(i))
            }
            values
        }
    }

    private inline fun <reified T> loadListFromAsset(filename: String): List<T> {
        val json = assetManager.open(filename).bufferedReader().use{ it.readText()}
        val listType = Types.newParameterizedType(List::class.java, T::class.java)
        val adapter: JsonAdapter<List<T>> = moshi.adapter(listType)
        return adapter.fromJson(json) ?: emptyList()
    }

    @Suppress("SameParameterValue")
    private fun getNotFoundException(message: String) : HttpException =
        message.toResponseBody("text/plain".toMediaType()).let {
            Response.error<Nothing>(NOT_FOUND, it)
        }.let {
            HttpException(it)
        }

    private fun getBadRequestException(message: String) : HttpException =
        message.toResponseBody("text/plain".toMediaType()).let {
            Response.error<Nothing>(BAD_REQUEST, it)
        }.let {
            HttpException(it)
        }

    private fun getInternalServerErrorException(message: String) : HttpException =
        message.toResponseBody("text/plain".toMediaType()).let {
            Response.error<Nothing>(INTERNAL_SERVER_ERROR, it)
        }.let {
            HttpException(it)
        }

    private enum class CategoryProducts(
        val categoryId: String,
        val productsFileName: String
    ) {
        BRA("category_bra", "products_bra.json"),
        PANTIES("category_panties", "products_panties.json"),
        LINGERIE("category_lingerie", "products_lingerie.json"),
        EROTIC("category_erotic", "products_erotic.json"),
        SWIM("category_swim", "products_swim.json"),
        CORSET("category_corsets", "products_corset.json"),
        GARTERBELT("category_garter_belts", "products_garter_belts.json"),
        BABYDOLL("category_babydolls", "products_babydoll.json"),
        STOCKINGS("category_stockings", "products_stockings.json"),
        PANTYHOSE("category_pantyhose", "products_pantyhose.json");

        companion object {

            fun findByCategoryId(id: String) = values().find { it.categoryId == id }
        }
    }

    companion object {
        private const val DELAY_LOADING = 500L
    }
}
