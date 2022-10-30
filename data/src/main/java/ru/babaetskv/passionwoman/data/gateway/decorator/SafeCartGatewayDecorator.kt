package ru.babaetskv.passionwoman.data.gateway.decorator

import ru.babaetskv.passionwoman.domain.gateway.exception.GatewayExceptionProvider
import ru.babaetskv.passionwoman.domain.gateway.CartGateway
import ru.babaetskv.passionwoman.domain.model.Cart
import ru.babaetskv.passionwoman.domain.model.CartItem
import ru.babaetskv.passionwoman.domain.model.base.Transformable

class SafeCartGatewayDecorator(
    private val gateway: CartGateway,
    private val exceptionProvider: GatewayExceptionProvider
) : CartGateway {

    override suspend fun addToCart(item: CartItem): Transformable<Unit, Cart> =
        try {
            gateway.addToCart(item)
        } catch (e: Exception) {
            throw exceptionProvider.getGatewayException(e)
        }

    override suspend fun checkout(): Transformable<Unit, Cart> =
        try {
            gateway.checkout()
        } catch (e: Exception) {
            throw exceptionProvider.getGatewayException(e)
        }

    override suspend fun getCart(): Transformable<Unit, Cart> =
        try {
            gateway.getCart()
        } catch (e: Exception) {
            throw exceptionProvider.getGatewayException(e)
        }

    override suspend fun removeFromCart(item: CartItem): Transformable<Unit, Cart> =
        try {
            gateway.removeFromCart(item)
        } catch (e: Exception) {
            throw exceptionProvider.getGatewayException(e)
        }
}
