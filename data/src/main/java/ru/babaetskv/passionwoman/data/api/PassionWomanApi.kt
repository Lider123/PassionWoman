package ru.babaetskv.passionwoman.data.api

import retrofit2.http.GET
import retrofit2.http.Query
import ru.babaetskv.passionwoman.data.model.CategoryModel
import ru.babaetskv.passionwoman.data.model.ProductModel

interface PassionWomanApi {

    @GET("api/catalog/categories")
    suspend fun getCategories(): List<CategoryModel>

    @GET("api/catalog/products")
    suspend fun getProducts(
        @Query("category") categoryId: String
    ): List<ProductModel>
}
