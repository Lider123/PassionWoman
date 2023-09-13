package ru.babaetskv.passionwoman.data.gateway.decorator.base

import ru.babaetskv.passionwoman.domain.gateway.CartGateway
import ru.babaetskv.passionwoman.domain.model.Cart
import ru.babaetskv.passionwoman.domain.model.CartItem
import ru.babaetskv.passionwoman.domain.model.CheckoutResult
import ru.babaetskv.passionwoman.domain.model.base.Transformable

abstract class CartGatewayDecorator(
    val gateway: CartGateway
) : CartGateway {

    override suspend fun addToCart(item: CartItem): Transformable<Unit, Cart> =
        gateway.addToCart(item)

    override suspend fun checkout(): Transformable<Unit, CheckoutResult> = gateway.checkout()

    override suspend fun getCart(): Transformable<Unit, Cart> = gateway.getCart()

    override suspend fun removeFromCart(item: CartItem): Transformable<Unit, Cart> =
        gateway.removeFromCart(item)
}
