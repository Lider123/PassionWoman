package ru.babaetskv.passionwoman.domain.gateway

import ru.babaetskv.passionwoman.domain.model.Cart
import ru.babaetskv.passionwoman.domain.model.CartItem
import ru.babaetskv.passionwoman.domain.model.base.Transformable

interface CartGateway {
    suspend fun checkout(): Transformable<Unit, Cart>
    suspend fun getCart(): Transformable<Unit, Cart>
    suspend fun addToCart(item: CartItem): Transformable<Unit, Cart>
    suspend fun removeFromCart(item: CartItem): Transformable<Unit, Cart>
}
