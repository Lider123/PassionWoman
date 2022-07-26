package ru.babaetskv.passionwoman.domain.usecase

import ru.babaetskv.passionwoman.domain.StringProvider
import ru.babaetskv.passionwoman.domain.exceptions.UseCaseException
import ru.babaetskv.passionwoman.domain.model.CartItem
import ru.babaetskv.passionwoman.domain.usecase.base.ActionUseCase

interface AddToCartUseCase : ActionUseCase<CartItem> {

    class AddToCartException(
        cause: Exception,
        stringProvider: StringProvider
    ) : UseCaseException.Action(cause, stringProvider.ADD_TO_CART_ERROR)
}
