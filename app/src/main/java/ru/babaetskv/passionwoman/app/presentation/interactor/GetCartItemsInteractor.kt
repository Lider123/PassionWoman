package ru.babaetskv.passionwoman.app.presentation.interactor

import ru.babaetskv.passionwoman.app.presentation.interactor.base.BaseInteractor
import ru.babaetskv.passionwoman.domain.StringProvider
import ru.babaetskv.passionwoman.domain.cache.base.ListCache
import ru.babaetskv.passionwoman.domain.model.CartItem
import ru.babaetskv.passionwoman.domain.usecase.GetCartItemsUseCase

class GetCartItemsInteractor(
    private val cartItemsCache: ListCache<CartItem>,
    private val stringProvider: StringProvider
) : BaseInteractor<Unit, List<CartItem>>(), GetCartItemsUseCase {

    override fun getUseCaseException(cause: Exception): Exception =
        GetCartItemsUseCase.GetCartItemsException(cause, stringProvider)

    override suspend fun run(params: Unit): List<CartItem> {
        val items = cartItemsCache.get()
        if (items.isNullOrEmpty()) throw GetCartItemsUseCase.EmptyCartItemsException(stringProvider)

        return items
    }
}
