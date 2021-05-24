package ru.babaetskv.passionwoman.data.api

import android.content.res.AssetManager
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import ru.babaetskv.passionwoman.data.model.CategoryModel
import ru.babaetskv.passionwoman.data.model.ProductModel

class PassionWomanApiImpl(
    private val moshi: Moshi,
    private val assetManager: AssetManager
) : PassionWomanApi {

    override suspend fun getCategories(): List<CategoryModel> = withContext(Dispatchers.IO) {
        delay(DELAY_LOADING)
        val json = assetManager.open("categories.json").bufferedReader().use{ it.readText()}
        val listType = Types.newParameterizedType(List::class.java, CategoryModel::class.java)
        val adapter: JsonAdapter<List<CategoryModel>> = moshi.adapter(listType)
        return@withContext (adapter.fromJson(json) ?: emptyList())
    }

    override suspend fun getProducts(categoryId: String): List<ProductModel> = withContext(Dispatchers.IO) {
        delay(DELAY_LOADING)
        val filename = CategoryProducts.findByCategoryId(categoryId)!!.productsFileName
        val json = assetManager.open(filename).bufferedReader().use{ it.readText()}
        val listType = Types.newParameterizedType(List::class.java, ProductModel::class.java)
        val adapter: JsonAdapter<List<ProductModel>> = moshi.adapter(listType)
        return@withContext adapter.fromJson(json) ?: emptyList()
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