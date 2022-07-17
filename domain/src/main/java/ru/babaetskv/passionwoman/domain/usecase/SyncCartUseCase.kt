package ru.babaetskv.passionwoman.domain.usecase

import ru.babaetskv.passionwoman.domain.StringProvider
import ru.babaetskv.passionwoman.domain.exceptions.EmptyDataException
import ru.babaetskv.passionwoman.domain.exceptions.NetworkDataException
import ru.babaetskv.passionwoman.domain.model.Cart
import ru.babaetskv.passionwoman.domain.model.CartItem
import ru.babaetskv.passionwoman.domain.usecase.base.ActionUseCase
import ru.babaetskv.passionwoman.domain.usecase.base.NoParamsUseCase

interface SyncCartUseCase : ActionUseCase<Unit> {

    class SyncCartException(
        cause: Exception?,
        stringProvider: StringProvider
    ) : NetworkDataException(stringProvider.GET_CART_ITEMS_ERROR, cause)

    class EmptyCartException(
        stringProvider: StringProvider
    ) : EmptyDataException(stringProvider.EMPTY_CART_ITEMS_ERROR)
}
