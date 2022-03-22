package ru.babaetskv.passionwoman.app.presentation.interactor

import ru.babaetskv.passionwoman.app.presentation.interactor.base.BaseInteractor
import ru.babaetskv.passionwoman.domain.StringProvider
import ru.babaetskv.passionwoman.domain.cache.base.ListCache
import ru.babaetskv.passionwoman.domain.model.CartItem
import ru.babaetskv.passionwoman.domain.usecase.AddToCartUseCase

class AddTocartInteractor(
    private val cartItemsCache: ListCache<CartItem>,
    private val stringProvider: StringProvider
) : BaseInteractor<CartItem, Unit>(), AddToCartUseCase {

    override fun getUseCaseException(cause: Exception): Exception =
        AddToCartUseCase.AddToCartException(cause, stringProvider)

    override suspend fun run(params: CartItem) {
        cartItemsCache.add(params)
    }
}
