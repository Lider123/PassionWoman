package ru.babaetskv.passionwoman.app.presentation.interactor

import ru.babaetskv.passionwoman.app.presentation.interactor.base.BaseInteractor
import ru.babaetskv.passionwoman.domain.StringProvider
import ru.babaetskv.passionwoman.domain.dataflow.CartFlow
import ru.babaetskv.passionwoman.domain.gateway.CartGateway
import ru.babaetskv.passionwoman.domain.model.CartItem
import ru.babaetskv.passionwoman.domain.usecase.RemoveFromCartUseCase
import ru.babaetskv.passionwoman.domain.utils.transform

class RemoveFromCartInteractor(
    private val cartGateway: CartGateway,
    private val cartFlow: CartFlow,
    private val stringProvider: StringProvider
) : BaseInteractor<CartItem, Unit>(), RemoveFromCartUseCase {

    override fun getUseCaseException(cause: Exception): Exception =
        RemoveFromCartUseCase.RemoveFromCartException(cause, stringProvider)

    override suspend fun run(params: CartItem) {
        val cart = cartGateway.removeFromCart(params).transform()
        cartFlow.send(cart)
    }
}
