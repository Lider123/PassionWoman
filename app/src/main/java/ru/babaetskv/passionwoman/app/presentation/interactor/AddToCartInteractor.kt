package ru.babaetskv.passionwoman.app.presentation.interactor

import ru.babaetskv.passionwoman.app.presentation.interactor.base.BaseInteractor
import ru.babaetskv.passionwoman.domain.StringProvider
import ru.babaetskv.passionwoman.domain.dataflow.CartFlow
import ru.babaetskv.passionwoman.domain.gateway.CartGateway
import ru.babaetskv.passionwoman.domain.model.CartItem
import ru.babaetskv.passionwoman.domain.usecase.AddToCartUseCase
import ru.babaetskv.passionwoman.domain.utils.transform

class AddToCartInteractor(
    private val cartGateway: CartGateway,
    private val cartFlow: CartFlow,
    private val stringProvider: StringProvider
) : BaseInteractor<CartItem, Unit>(), AddToCartUseCase {

    override fun getUseCaseException(cause: Exception): Exception =
        AddToCartUseCase.AddToCartException(cause, stringProvider)

    override suspend fun run(params: CartItem) {
        val cart = cartGateway.addToCart(params).transform()
        cartFlow.send(cart)
    }
}
