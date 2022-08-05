package ru.babaetskv.passionwoman.data.api

import retrofit2.http.*
import ru.babaetskv.passionwoman.data.model.*

interface CommonApi {

    @POST("api/auth")
    suspend fun authorize(
        @Body body: AccessTokenModel
    ): AuthTokenModel

    @GET("api/catalog/categories")
    suspend fun getCategories(): List<CategoryModel>

    @GET("api/catalog/promotions")
    suspend fun getPromotions(): List<PromotionModel>

    @GET("api/catalog/products")
    suspend fun getProducts(
        @Query("category") categoryId: Int?,
        @Query("query") query: String,
        @Query("filters") filters: String,
        @Query("sorting") sorting: String,
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): ProductsPagedResponseModel

    @GET("api/catalog/productsByIds")
    suspend fun getProductsByIds(
        @Query("ids") ids: String
    ): List<ProductModel>

    @GET("api/catalog/brands/popular")
    suspend fun getPopularBrands(count: Int): List<BrandModel>

    @GET("api/catalog/products/{productId}")
    suspend fun getProduct(
        @Path("productId") productId: Int
    ): ProductModel

    @GET("api/catalog/stories")
    suspend fun getStories(): List<StoryModel>
}
