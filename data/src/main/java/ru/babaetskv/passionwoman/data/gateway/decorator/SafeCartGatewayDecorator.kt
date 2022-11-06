package ru.babaetskv.passionwoman.data.gateway.decorator

import ru.babaetskv.passionwoman.data.gateway.decorator.base.CartGatewayDecorator
import ru.babaetskv.passionwoman.domain.gateway.exception.GatewayExceptionProvider
import ru.babaetskv.passionwoman.domain.gateway.CartGateway
import ru.babaetskv.passionwoman.domain.model.Cart
import ru.babaetskv.passionwoman.domain.model.CartItem
import ru.babaetskv.passionwoman.domain.model.base.Transformable

class SafeCartGatewayDecorator(
    gateway: CartGateway,
    private val exceptionProvider: GatewayExceptionProvider
) : CartGatewayDecorator(gateway) {

    override suspend fun addToCart(item: CartItem): Transformable<Unit, Cart> =
        try {
            super.addToCart(item)
        } catch (e: Exception) {
            throw exceptionProvider.getGatewayException(e)
        }

    override suspend fun checkout(): Transformable<Unit, Cart> =
        try {
            super.checkout()
        } catch (e: Exception) {
            throw exceptionProvider.getGatewayException(e)
        }

    override suspend fun getCart(): Transformable<Unit, Cart> =
        try {
            super.getCart()
        } catch (e: Exception) {
            throw exceptionProvider.getGatewayException(e)
        }

    override suspend fun removeFromCart(item: CartItem): Transformable<Unit, Cart> =
        try {
            super.removeFromCart(item)
        } catch (e: Exception) {
            throw exceptionProvider.getGatewayException(e)
        }
}
