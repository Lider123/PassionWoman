package ru.babaetskv.passionwoman.app.presentation.interactor

import ru.babaetskv.passionwoman.app.presentation.interactor.base.BaseInteractor
import ru.babaetskv.passionwoman.domain.StringProvider
import ru.babaetskv.passionwoman.domain.dataflow.CartFlow
import ru.babaetskv.passionwoman.domain.gateway.CartGateway
import ru.babaetskv.passionwoman.domain.usecase.SyncCartUseCase
import ru.babaetskv.passionwoman.domain.utils.transform

class SyncCartInteractor(
    private val cartGateway: CartGateway,
    private val cartFlow: CartFlow,
    private val stringProvider: StringProvider
) : BaseInteractor<Unit, Unit>(), SyncCartUseCase {

    override fun getUseCaseException(cause: Exception): Exception =
        SyncCartUseCase.SyncCartException(cause, stringProvider)

    override suspend fun run(params: Unit) {
        val cart = cartGateway.getCart().transform()
        cartFlow.send(cart)
    }
}
