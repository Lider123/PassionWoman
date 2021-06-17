package ru.babaetskv.passionwoman.data.api

import android.content.res.AssetManager
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import ru.babaetskv.passionwoman.data.model.*
import ru.babaetskv.passionwoman.domain.model.Filters
import ru.babaetskv.passionwoman.domain.model.Sorting

class PassionWomanApiImpl(
    private val moshi: Moshi,
    private val assetManager: AssetManager
) : PassionWomanApi {
    private var profileMock: ProfileModel? = null

    override suspend fun getCategories(): List<CategoryModel> = withContext(Dispatchers.IO) {
        delay(DELAY_LOADING)
        val json = assetManager.open("categories.json").bufferedReader().use{ it.readText()}
        val listType = Types.newParameterizedType(List::class.java, CategoryModel::class.java)
        val adapter: JsonAdapter<List<CategoryModel>> = moshi.adapter(listType)
        return@withContext (adapter.fromJson(json) ?: emptyList())
    }

    override suspend fun getPromotions(): List<PromotionModel> = withContext(Dispatchers.IO) {
        delay(DELAY_LOADING)
        val json = assetManager.open("promotions.json").bufferedReader().use{ it.readText()}
        val listType = Types.newParameterizedType(List::class.java, PromotionModel::class.java)
        val adapter: JsonAdapter<List<PromotionModel>> = moshi.adapter(listType)
        return@withContext (adapter.fromJson(json) ?: emptyList())
    }

    override suspend fun getProducts(
        categoryId: String?,
        filters: String,
        sorting: String,
        limit: Int,
        offset: Int
    ): List<ProductModel> = withContext(Dispatchers.IO) {
        delay(DELAY_LOADING)
        val filtersObject = moshi.adapter(FiltersModel::class.java).fromJson(filters)?.toFilters()
        val filename = if (categoryId != null) {
            CategoryProducts.findByCategoryId(categoryId)!!.productsFileName
        } else {
            val sortingObject = Sorting.findValueByApiName(sorting)
            when {
                filtersObject?.discountOnly == true -> "products_sale.json"
                listOf(Sorting.POPULARITY_DESC, Sorting.POPULARITY_ASC).contains(sortingObject) -> "products_popular.json"
                listOf(Sorting.NEW_DESC, Sorting.NEW_ASC).contains(sortingObject) -> "products_new.json"
                else -> "products_popular.json"
            }
        }
        val json = assetManager.open(filename).bufferedReader().use{ it.readText()}
        val listType = Types.newParameterizedType(List::class.java, ProductModel::class.java)
        val adapter: JsonAdapter<List<ProductModel>> = moshi.adapter(listType)
        var products = adapter.fromJson(json) ?: emptyList()
        if (filtersObject != null) {
            products = products.filter {
                it.filter(filtersObject)
            }
        }
        val pagingIndices = IntRange(offset, offset + limit - 1)
        return@withContext products.slice(products.indices.intersect(pagingIndices))
    }

    override suspend fun getBrands(): List<BrandModel> = withContext(Dispatchers.IO) {
        delay(DELAY_LOADING)
        val json = assetManager.open("brands.json").bufferedReader().use{ it.readText()}
        val listType = Types.newParameterizedType(List::class.java, BrandModel::class.java)
        val adapter: JsonAdapter<List<BrandModel>> = moshi.adapter(listType)
        return@withContext (adapter.fromJson(json) ?: emptyList())
    }

    override suspend fun getProfile(): ProfileModel = withContext(Dispatchers.IO) {
        delay(DELAY_LOADING)
        return@withContext if (profileMock == null) {
            val json = assetManager.open("profile.json").bufferedReader().use{ it.readText()}
            val adapter: JsonAdapter<ProfileModel> = moshi.adapter(ProfileModel::class.java)
            adapter.fromJson(json)!!.also { profileMock = it }
        } else profileMock!!
    }

    override suspend fun updateProfile(body: ProfileModel) = withContext(Dispatchers.IO) {
        delay(DELAY_LOADING)
        profileMock = body
    }

    override suspend fun uploadAvatar(image: MultipartBody.Part) {
        delay(DELAY_LOADING)
        // TODO: think up how to save image
    }

    private fun ProductModel.filter(filters: Filters): Boolean {
        if (filters.discountOnly && priceWithDiscount == price) return false

        return true
    }

    enum class CategoryProducts(val categoryId: String, val productsFileName: String) {
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