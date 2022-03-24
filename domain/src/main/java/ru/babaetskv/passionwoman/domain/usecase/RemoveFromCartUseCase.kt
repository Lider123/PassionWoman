package ru.babaetskv.passionwoman.domain.usecase

import ru.babaetskv.passionwoman.domain.StringProvider
import ru.babaetskv.passionwoman.domain.exceptions.NetworkActionException
import ru.babaetskv.passionwoman.domain.model.CartItem
import ru.babaetskv.passionwoman.domain.usecase.base.ActionUseCase

interface RemoveFromCartUseCase : ActionUseCase<CartItem> {

    class RemoveFromCartException(
        cause: Exception?,
        stringProvider: StringProvider
    ) : NetworkActionException(stringProvider.REMOVE_FROM_CART_ERROR, cause)
}
