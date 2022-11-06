package ru.babaetskv.passionwoman.data.api.decorator.base

import okhttp3.MultipartBody
import ru.babaetskv.passionwoman.data.api.AuthApi
import ru.babaetskv.passionwoman.data.model.CartItemModel
import ru.babaetskv.passionwoman.data.model.CartModel
import ru.babaetskv.passionwoman.data.model.OrderModel
import ru.babaetskv.passionwoman.data.model.ProfileModel

abstract class AuthApiDecorator(
    val api: AuthApi
) : AuthApi {

    override suspend fun addToCart(item: CartItemModel): CartModel = api.addToCart(item)

    override suspend fun checkout(): CartModel = api.checkout()

    override suspend fun getCart(): CartModel = api.getCart()

    override suspend fun getFavoriteIds(): List<Long> = api.getFavoriteIds()

    override suspend fun getOrders(): List<OrderModel> = api.getOrders()

    override suspend fun getProfile(): ProfileModel = api.getProfile()

    override suspend fun removeFromCart(item: CartItemModel): CartModel = api.removeFromCart(item)

    override suspend fun setFavoriteIds(ids: List<Long>) {
        api.setFavoriteIds(ids)
    }

    override suspend fun updateProfile(body: ProfileModel) {
        api.updateProfile(body)
    }

    override suspend fun uploadAvatar(image: MultipartBody.Part) {
        api.uploadAvatar(image)
    }
}