package ru.babaetskv.passionwoman.data.api.decorator

import okhttp3.MultipartBody
import ru.babaetskv.passionwoman.data.api.AuthApi
import ru.babaetskv.passionwoman.data.api.decorator.base.AuthApiDecorator
import ru.babaetskv.passionwoman.data.api.exception.ApiExceptionProvider
import ru.babaetskv.passionwoman.data.model.CartItemModel
import ru.babaetskv.passionwoman.data.model.CartModel
import ru.babaetskv.passionwoman.data.model.CheckoutResultModel
import ru.babaetskv.passionwoman.data.model.OrderModel
import ru.babaetskv.passionwoman.data.model.ProfileModel
import ru.babaetskv.passionwoman.domain.preferences.AuthPreferences
import timber.log.Timber

class CheckTokenAuthApiDecorator(
    private val authPreferences: AuthPreferences,
    private val exceptionProvider: ApiExceptionProvider,
    api: AuthApi
) : AuthApiDecorator(api) {

    override suspend fun addToCart(item: CartItemModel): CartModel {
        checkToken()
        return super.addToCart(item)
    }

    override suspend fun checkout(): CheckoutResultModel {
        checkToken()
        return super.checkout()
    }

    override suspend fun getCart(): CartModel {
        checkToken()
        return super.getCart()
    }

    override suspend fun getFavoriteIds(): List<Long> {
        checkToken()
        return super.getFavoriteIds()
    }

    override suspend fun getOrders(): List<OrderModel> {
        checkToken()
        return super.getOrders()
    }

    override suspend fun getProfile(): ProfileModel {
        checkToken()
        return super.getProfile()
    }

    override suspend fun removeFromCart(item: CartItemModel): CartModel {
        checkToken()
        return super.removeFromCart(item)
    }

    override suspend fun setFavoriteIds(ids: List<Long>) {
        checkToken()
        super.setFavoriteIds(ids)
    }

    override suspend fun updateProfile(body: ProfileModel) {
        checkToken()
        super.updateProfile(body)
    }

    override suspend fun uploadAvatar(image: MultipartBody.Part) {
        checkToken()
        super.uploadAvatar(image)
    }

    override suspend fun registerPushToken(token: MultipartBody.Part) {
        checkToken()
        super.registerPushToken(token)
    }

    override suspend fun unregisterPushToken(token: MultipartBody.Part) {
        checkToken()
        super.unregisterPushToken(token)
    }

    private fun checkToken() {
        val userToken = authPreferences.authToken
        if (userToken != TOKEN) {
            Timber.e("Incorrect token: $userToken")
            throw exceptionProvider.getUnauthorizedException("Unauthorized")
        }
    }

    companion object {
        @JvmStatic
        val TOKEN = "token"
    }
}
