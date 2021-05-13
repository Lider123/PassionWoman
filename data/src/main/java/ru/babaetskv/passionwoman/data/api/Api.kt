package ru.babaetskv.passionwoman.data.api

import ru.babaetskv.passionwoman.data.model.CategoryModel
import ru.babaetskv.passionwoman.data.model.ProductModel

interface Api {

    suspend fun getCategories(): List<CategoryModel>

    suspend fun getProducts(categoryId: String): List<ProductModel>
}
