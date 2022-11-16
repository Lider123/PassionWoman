package ru.babaetskv.passionwoman.data.api

import okhttp3.MultipartBody
import retrofit2.http.*
import ru.babaetskv.passionwoman.data.model.*

interface AuthApi {

    @GET("api/profile/favoriteIds")
    suspend fun getFavoriteIds(): List<Long>

    @Multipart
    @POST("api/profile/favoriteIds")
    suspend fun setFavoriteIds(
        @Part("favoriteIds") ids: List<Long>
    )

    @GET("api/profile")
    suspend fun getProfile(): ProfileModel

    @PUT("api/profile")
    suspend fun updateProfile(
        @Body body: ProfileModel
    )

    @Multipart
    @POST("api/profile/avatar")
    suspend fun uploadAvatar(
        @Part image: MultipartBody.Part
    )

    @GET("api/profile/orders")
    suspend fun getOrders(): List<OrderModel>

    @POST("api/cart/checkout")
    suspend fun checkout(): CartModel

    @GET("api/cart")
    suspend fun getCart(): CartModel

    @POST("api/cart/add")
    suspend fun addToCart(
        @Body item: CartItemModel
    ): CartModel

    @POST("api/cart/remove")
    suspend fun removeFromCart(
        @Body item: CartItemModel
    ): CartModel

    @Multipart
    @POST("api/push/token")
    suspend fun registerPushToken(
        @Part("token") token: MultipartBody.Part
    )

    @Multipart
    @HTTP(method = "DELETE", path = "api/push/token", hasBody = true)
    suspend fun unregisterPushToken(
        @Part("token") token: MultipartBody.Part
    )
}
