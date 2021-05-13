package ru.babaetskv.passionwoman.data.api

import android.content.res.AssetManager
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.babaetskv.passionwoman.data.model.CategoryModel
import ru.babaetskv.passionwoman.data.model.ProductModel

class ApiImpl(
    private val assetManager: AssetManager
) : Api {
    private val moshi =
        Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

    override suspend fun getCategories(): List<CategoryModel> = withContext(Dispatchers.IO) {
        val json = assetManager.open("categories.json").bufferedReader().use{ it.readText()}
        val listType = Types.newParameterizedType(List::class.java, CategoryModel::class.java)
        val adapter: JsonAdapter<List<CategoryModel>> = moshi.adapter(listType)
        return@withContext (adapter.fromJson(json) ?: emptyList())
    }

    override suspend fun getProducts(categoryId: String): List<ProductModel> = withContext(Dispatchers.IO) {
        val filename = CategoryProducts.findByCategoryId(categoryId)!!.productsFileName
        val json = assetManager.open(filename).bufferedReader().use{ it.readText()}
        val listType = Types.newParameterizedType(List::class.java, ProductModel::class.java)
        val adapter: JsonAdapter<List<ProductModel>> = moshi.adapter(listType)
        return@withContext (adapter.fromJson(json) ?: emptyList())
    }

    enum class CategoryProducts(val categoryId: String, val productsFileName: String) {
        BRA("category_bra", "products_bra.json"),
        PANTIES("category_panties", "products_panties.json");

        companion object {

            fun findByCategoryId(id: String) = values().find { it.categoryId == id }
        }
    }
}