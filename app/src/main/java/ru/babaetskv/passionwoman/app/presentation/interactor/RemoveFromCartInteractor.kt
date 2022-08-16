package ru.babaetskv.passionwoman.app.presentation.interactor

import ru.babaetskv.passionwoman.app.presentation.interactor.base.BaseInteractor
import ru.babaetskv.passionwoman.domain.StringProvider
import ru.babaetskv.passionwoman.domain.dataflow.CartFlow
import ru.babaetskv.passionwoman.domain.exceptions.UseCaseException
import ru.babaetskv.passionwoman.domain.gateway.CartGateway
import ru.babaetskv.passionwoman.domain.model.CartItem
import ru.babaetskv.passionwoman.domain.model.base.Transformable.Companion.transform
import ru.babaetskv.passionwoman.domain.usecase.RemoveFromCartUseCase

class RemoveFromCartInteractor(
    private val cartGateway: CartGateway,
    private val cartFlow: CartFlow,
    private val stringProvider: StringProvider
) : BaseInteractor<CartItem, Unit>(), RemoveFromCartUseCase {
    override val emptyException: UseCaseException.EmptyData? = null

    override fun transformException(cause: Exception): UseCaseException =
        RemoveFromCartUseCase.RemoveFromCartException(cause, stringProvider)

    override suspend fun run(params: CartItem) {
        val cart = cartGateway.removeFromCart(params).transform()
        cartFlow.send(cart)
    }
}
