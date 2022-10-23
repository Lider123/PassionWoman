package ru.babaetskv.passionwoman.data.api.decorator

import kotlinx.coroutines.delay
import okhttp3.MultipartBody
import ru.babaetskv.passionwoman.data.api.AuthApi
import ru.babaetskv.passionwoman.data.model.CartItemModel
import ru.babaetskv.passionwoman.data.model.CartModel
import ru.babaetskv.passionwoman.data.model.OrderModel
import ru.babaetskv.passionwoman.data.model.ProfileModel

class DelayAuthApiDecorator(api: AuthApi) : AuthApiDecorator(api) {

    override suspend fun addToCart(item: CartItemModel): CartModel {
        delay(DELAY_LOADING)
        return api.addToCart(item)
    }

    override suspend fun checkout(): CartModel {
        delay(DELAY_LOADING)
        return api.checkout()
    }

    override suspend fun getCart(): CartModel {
        delay(DELAY_LOADING)
        return api.getCart()
    }

    override suspend fun getFavoriteIds(): List<Long> {
        delay(DELAY_LOADING)
        return api.getFavoriteIds()
    }

    override suspend fun getOrders(): List<OrderModel> {
        delay(DELAY_LOADING)
        return api.getOrders()
    }

    override suspend fun getProfile(): ProfileModel {
        delay(DELAY_LOADING)
        return api.getProfile()
    }

    override suspend fun removeFromCart(item: CartItemModel): CartModel {
        delay(DELAY_LOADING)
        return api.removeFromCart(item)
    }

    override suspend fun setFavoriteIds(ids: List<Long>) {
        delay(DELAY_LOADING)
        api.setFavoriteIds(ids)
    }

    override suspend fun updateProfile(body: ProfileModel) {
        delay(DELAY_LOADING)
        api.updateProfile(body)
    }

    override suspend fun uploadAvatar(image: MultipartBody.Part) {
        delay(DELAY_LOADING)
        api.uploadAvatar(image)
    }

    companion object {
        private const val DELAY_LOADING = 500L
    }
}
