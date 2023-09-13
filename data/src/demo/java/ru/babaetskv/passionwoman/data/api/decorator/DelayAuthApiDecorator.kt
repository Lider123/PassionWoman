package ru.babaetskv.passionwoman.data.api.decorator

import kotlinx.coroutines.delay
import okhttp3.MultipartBody
import ru.babaetskv.passionwoman.data.api.AuthApi
import ru.babaetskv.passionwoman.data.api.decorator.base.AuthApiDecorator
import ru.babaetskv.passionwoman.data.model.CartItemModel
import ru.babaetskv.passionwoman.data.model.CartModel
import ru.babaetskv.passionwoman.data.model.CheckoutResultModel
import ru.babaetskv.passionwoman.data.model.OrderModel
import ru.babaetskv.passionwoman.data.model.ProfileModel

class DelayAuthApiDecorator(
    api: AuthApi
) : AuthApiDecorator(api) {

    override suspend fun addToCart(item: CartItemModel): CartModel {
        delay(DELAY_LOADING)
        return super.addToCart(item)
    }

    override suspend fun checkout(): CheckoutResultModel {
        delay(DELAY_LOADING)
        return super.checkout()
    }

    override suspend fun getCart(): CartModel {
        delay(DELAY_LOADING)
        return super.getCart()
    }

    override suspend fun getFavoriteIds(): List<Long> {
        delay(DELAY_LOADING)
        return super.getFavoriteIds()
    }

    override suspend fun getOrders(): List<OrderModel> {
        delay(DELAY_LOADING)
        return super.getOrders()
    }

    override suspend fun getProfile(): ProfileModel {
        delay(DELAY_LOADING)
        return super.getProfile()
    }

    override suspend fun removeFromCart(item: CartItemModel): CartModel {
        delay(DELAY_LOADING)
        return super.removeFromCart(item)
    }

    override suspend fun setFavoriteIds(ids: List<Long>) {
        delay(DELAY_LOADING)
        super.setFavoriteIds(ids)
    }

    override suspend fun updateProfile(body: ProfileModel) {
        delay(DELAY_LOADING)
        super.updateProfile(body)
    }

    override suspend fun uploadAvatar(image: MultipartBody.Part) {
        delay(DELAY_LOADING)
        super.uploadAvatar(image)
    }

    override suspend fun registerPushToken(token: MultipartBody.Part) {
        delay(DELAY_LOADING)
        super.registerPushToken(token)
    }

    override suspend fun unregisterPushToken(token: MultipartBody.Part) {
        delay(DELAY_LOADING)
        super.unregisterPushToken(token)
    }

    companion object {
        private const val DELAY_LOADING = 500L
    }
}
