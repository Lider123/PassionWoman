package ru.babaetskv.passionwoman.data.api

import okhttp3.MultipartBody
import retrofit2.http.*
import ru.babaetskv.passionwoman.data.model.CategoryModel
import ru.babaetskv.passionwoman.data.model.ProductModel
import ru.babaetskv.passionwoman.data.model.ProfileModel

interface PassionWomanApi {

    @GET("api/catalog/categories")
    suspend fun getCategories(): List<CategoryModel>

    @GET("api/catalog/products")
    suspend fun getProducts(
        @Query("category") categoryId: String
    ): List<ProductModel>

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
