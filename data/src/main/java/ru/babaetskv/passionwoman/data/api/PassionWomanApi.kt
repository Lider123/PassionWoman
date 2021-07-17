package ru.babaetskv.passionwoman.data.api

import okhttp3.MultipartBody
import retrofit2.http.*
import ru.babaetskv.passionwoman.data.model.*

interface PassionWomanApi {

    @GET("api/catalog/categories")
    suspend fun getCategories(): List<CategoryModel>

    @GET("api/catalog/promotions")
    suspend fun getPromotions(): List<PromotionModel>

    @GET("api/catalog/products")
    suspend fun getProducts(
        @Query("category") categoryId: String?,
        @Query("filters") filters: String,
        @Query("sorting") sorting: String,
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): List<ProductModel>

    @GET("api/catalog/brands/popular")
    suspend fun getPopularBrands(): List<BrandModel>

    @GET("api/auth/profile")
    suspend fun getProfile(): ProfileModel

    @PUT("api/auth/profile")
    suspend fun updateProfile(
        @Body body: ProfileModel
    )

    @Multipart
    @POST("api/auth/avatar")
    suspend fun uploadAvatar(
        @Part image: MultipartBody.Part
    )
}
