package ru.babaetskv.passionwoman.data.gateway

import ru.babaetskv.passionwoman.data.api.AuthApi
import ru.babaetskv.passionwoman.data.model.CartItemModel
import ru.babaetskv.passionwoman.domain.gateway.CartGateway
import ru.babaetskv.passionwoman.domain.model.CartItem

class CartGatewayImpl(
    private val authApi: AuthApi
) : CartGateway {
    // TODO: remove local cart and add the remote one

    override suspend fun checkout(items: List<CartItem>) {
        authApi.checkout(items.map(::CartItemModel))
    }
}
