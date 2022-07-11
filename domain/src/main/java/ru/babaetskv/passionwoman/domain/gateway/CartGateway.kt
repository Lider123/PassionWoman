package ru.babaetskv.passionwoman.domain.gateway

import ru.babaetskv.passionwoman.domain.model.CartItem

interface CartGateway {
    suspend fun checkout(items: List<CartItem>)
}
