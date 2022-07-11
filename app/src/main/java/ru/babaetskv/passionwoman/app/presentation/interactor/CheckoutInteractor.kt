package ru.babaetskv.passionwoman.app.presentation.interactor

import ru.babaetskv.passionwoman.app.presentation.interactor.base.BaseInteractor
import ru.babaetskv.passionwoman.domain.StringProvider
import ru.babaetskv.passionwoman.domain.cache.base.ListCache
import ru.babaetskv.passionwoman.domain.gateway.CartGateway
import ru.babaetskv.passionwoman.domain.model.CartItem
import ru.babaetskv.passionwoman.domain.usecase.CheckoutUseCase

class CheckoutInteractor(
    private val cartGateway: CartGateway,
    private val cartItemsCache: ListCache<CartItem>,
    private val stringProvider: StringProvider
) : BaseInteractor<List<CartItem>, Unit>(), CheckoutUseCase {

    override fun getUseCaseException(cause: Exception): Exception =
        CheckoutUseCase.CheckoutException(cause, stringProvider)

    override suspend fun run(params: List<CartItem>) {
        cartGateway.checkout(params)
        cartItemsCache.clear()
    }
}
