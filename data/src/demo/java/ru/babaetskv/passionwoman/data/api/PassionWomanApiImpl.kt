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
import retrofit2.HttpException
import retrofit2.Response
import ru.babaetskv.passionwoman.data.model.*
import ru.babaetskv.passionwoman.domain.interactor.exception.HttpCodes.NOT_FOUND
import ru.babaetskv.passionwoman.domain.model.Filters
import ru.babaetskv.passionwoman.domain.model.Sorting

class PassionWomanApiImpl(
    private val moshi: Moshi,
    private val assetManager: AssetManager
) : PassionWomanApi {
    private var profileMock: ProfileModel? = null
    private var favoriteIdsMock: List<String>? = null

    override suspend fun getCategories(): List<CategoryModel> = withContext(Dispatchers.IO) {
        delay(DELAY_LOADING)
        return@withContext loadListFromAsset("categories.json")
    }

    override suspend fun getPromotions(): List<PromotionModel> = withContext(Dispatchers.IO) {
        delay(DELAY_LOADING)
        return@withContext loadListFromAsset("promotions.json")
    }

    override suspend fun getProducts(
        categoryId: String?,
        filters: String,
        sorting: String,
        limit: Int,
        offset: Int
    ): ProductsPagedResponseModel = withContext(Dispatchers.IO) {
        delay(DELAY_LOADING)
        val filtersObject = moshi.adapter(FiltersModel::class.java).fromJson(filters)?.toFilters()
        val sortingObject = Sorting.findValueByApiName(sorting)
        val filename = if (categoryId != null) {
            CategoryProducts.findByCategoryId(categoryId)!!.productsFileName
        } else {
            when {
                filtersObject?.discountOnly == true -> "products_sale.json"
                sortingObject == Sorting.POPULARITY -> "products_popular.json"
                sortingObject == Sorting.NEW -> "products_new.json"
                else -> "products_popular.json"
            }
        }
        var products = loadListFromAsset<ProductModel>(filename)
        if (filtersObject != null) {
            products = products.filter {
                it.filter(filtersObject)
            }
        }
        val pagingIndices = IntRange(offset, offset + limit - 1)
        return@withContext products.slice(products.indices.intersect(pagingIndices)).let { result ->
            when (sortingObject) {
                Sorting.PRICE_ASC -> result.sortedBy { it.priceWithDiscount }
                Sorting.PRICE_DESC -> result.sortedByDescending { it.priceWithDiscount }
                else -> result
            }
        }.let {
            ProductsPagedResponseModel(
                products = it,
                total = products.size
            )
        }
    }

    override suspend fun getPopularBrands(): List<BrandModel> = withContext(Dispatchers.IO) {
        delay(DELAY_LOADING)
        return@withContext loadListFromAsset<BrandModel>("brands.json").take(8)
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

    private inline fun <reified T> loadObjectFromAsset(filename: String): T {
        val json = assetManager.open(filename).bufferedReader().use{ it.readText()}
        val adapter: JsonAdapter<T> = moshi.adapter(T::class.java)
        return adapter.fromJson(json)!!
    }

    private inline fun <reified T> loadListFromAsset(filename: String): List<T> {
        val json = assetManager.open(filename).bufferedReader().use{ it.readText()}
        val listType = Types.newParameterizedType(List::class.java, T::class.java)
        val adapter: JsonAdapter<List<T>> = moshi.adapter(listType)
        return adapter.fromJson(json) ?: emptyList()
    }

    private fun ProductModel.filter(filters: Filters): Boolean {
        if (filters.discountOnly && priceWithDiscount == price) return false

        return true
    }

    private fun getNotFoundException(message: String) : HttpException =
        message.toResponseBody("text/plain".toMediaType()).let {
            Response.error<Nothing>(NOT_FOUND, it)
        }.let {
            HttpException(it)
        }

    private enum class CategoryProducts(val categoryId: String, val productsFileName: String) {
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