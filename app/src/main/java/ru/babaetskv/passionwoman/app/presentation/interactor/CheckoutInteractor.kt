package ru.babaetskv.passionwoman.app.presentation.interactor

import ru.babaetskv.passionwoman.app.presentation.interactor.base.BaseInteractor
import ru.babaetskv.passionwoman.domain.StringProvider
import ru.babaetskv.passionwoman.domain.dataflow.CartFlow
import ru.babaetskv.passionwoman.domain.exceptions.UseCaseException
import ru.babaetskv.passionwoman.domain.gateway.CartGateway
import ru.babaetskv.passionwoman.domain.usecase.CheckoutUseCase
import ru.babaetskv.passionwoman.domain.utils.transform

class CheckoutInteractor(
    private val cartGateway: CartGateway,
    private val cartFlow: CartFlow,
    private val stringProvider: StringProvider
) : BaseInteractor<Unit, Unit>(), CheckoutUseCase {
    override val emptyException: UseCaseException.EmptyData? = null

    override fun transformException(cause: Exception): UseCaseException =
        CheckoutUseCase.CheckoutException(cause, stringProvider)

    override suspend fun run(params: Unit) {
        val cart = cartGateway.checkout().transform()
        cartFlow.send(cart)
    }
}
