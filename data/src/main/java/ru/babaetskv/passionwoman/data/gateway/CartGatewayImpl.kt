package ru.babaetskv.passionwoman.data.gateway

import ru.babaetskv.passionwoman.data.api.AuthApi
import ru.babaetskv.passionwoman.data.model.CartItemModel
import ru.babaetskv.passionwoman.domain.gateway.CartGateway
import ru.babaetskv.passionwoman.domain.model.Cart
import ru.babaetskv.passionwoman.domain.model.CartItem
import ru.babaetskv.passionwoman.domain.model.base.Transformable

class CartGatewayImpl(
    private val authApi: AuthApi
) : CartGateway {

    override suspend fun checkout(): Transformable<Unit, Cart> =
        authApi.checkout()

    override suspend fun addToCart(item: CartItem): Transformable<Unit, Cart> =
        authApi.addToCart(CartItemModel(item))

    override suspend fun removeFromCart(item: CartItem): Transformable<Unit, Cart> =
        authApi.removeFromCart(CartItemModel(item))

    override suspend fun getCart(): Transformable<Unit, Cart> =
        authApi.getCart()
}
