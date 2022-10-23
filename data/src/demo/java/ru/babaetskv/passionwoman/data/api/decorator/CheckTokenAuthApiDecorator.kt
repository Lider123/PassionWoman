package ru.babaetskv.passionwoman.data.api.decorator

import okhttp3.MultipartBody
import ru.babaetskv.passionwoman.data.api.ApiExceptionProvider
import ru.babaetskv.passionwoman.data.api.AuthApi
import ru.babaetskv.passionwoman.data.model.CartItemModel
import ru.babaetskv.passionwoman.data.model.CartModel
import ru.babaetskv.passionwoman.data.model.OrderModel
import ru.babaetskv.passionwoman.data.model.ProfileModel
import ru.babaetskv.passionwoman.domain.preferences.AuthPreferences
import timber.log.Timber

class CheckTokenAuthApiDecorator(
    private val authPreferences: AuthPreferences,
    api: AuthApi
) : AuthApiDecorator(api) {

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
        setFavoriteIds(ids)
    }

    override suspend fun updateProfile(body: ProfileModel) {
        checkToken()
        updateProfile(body)
    }

    override suspend fun uploadAvatar(image: MultipartBody.Part) {
        checkToken()
        uploadAvatar(image)
    }

    private fun checkToken() {
        val userToken = authPreferences.authToken
        if (userToken != TOKEN) {
            Timber.e("Incorrect token: $userToken")
            throw ApiExceptionProvider.getUnauthorizedException("Unauthorized")
        }
    }

    companion object {
        @JvmStatic
        val TOKEN = "token"
    }
}
