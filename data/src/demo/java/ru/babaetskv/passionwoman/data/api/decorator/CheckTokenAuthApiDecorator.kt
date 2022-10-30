package ru.babaetskv.passionwoman.data.api.decorator

import okhttp3.MultipartBody
import ru.babaetskv.passionwoman.data.api.AuthApi
import ru.babaetskv.passionwoman.data.api.exception.ApiExceptionProvider
import ru.babaetskv.passionwoman.data.model.CartItemModel
import ru.babaetskv.passionwoman.data.model.CartModel
import ru.babaetskv.passionwoman.data.model.OrderModel
import ru.babaetskv.passionwoman.data.model.ProfileModel
import ru.babaetskv.passionwoman.domain.preferences.AuthPreferences
import timber.log.Timber

class CheckTokenAuthApiDecorator(
    private val authPreferences: AuthPreferences,
    private val exceptionProvider: ApiExceptionProvider,
    private val api: AuthApi
) : AuthApi {

    override suspend fun addToCart(item: CartItemModel): CartModel {
        checkToken()
        return api.addToCart(item)
    }

    override suspend fun checkout(): CartModel {
        checkToken()
        return api.checkout()
    }

    override suspend fun getCart(): CartModel {
        checkToken()
        return api.getCart()
    }

    override suspend fun getFavoriteIds(): List<Long> {
        checkToken()
        return api.getFavoriteIds()
    }

    override suspend fun getOrders(): List<OrderModel> {
        checkToken()
        return api.getOrders()
    }

    override suspend fun getProfile(): ProfileModel {
        checkToken()
        return api.getProfile()
    }

    override suspend fun removeFromCart(item: CartItemModel): CartModel {
        checkToken()
        return api.removeFromCart(item)
    }

    override suspend fun setFavoriteIds(ids: List<Long>) {
        checkToken()
        api.setFavoriteIds(ids)
    }

    override suspend fun updateProfile(body: ProfileModel) {
        checkToken()
        api.updateProfile(body)
    }

    override suspend fun uploadAvatar(image: MultipartBody.Part) {
        checkToken()
        api.uploadAvatar(image)
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
