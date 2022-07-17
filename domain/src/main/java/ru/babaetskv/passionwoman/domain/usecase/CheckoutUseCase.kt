package ru.babaetskv.passionwoman.domain.usecase

import ru.babaetskv.passionwoman.domain.StringProvider
import ru.babaetskv.passionwoman.domain.exceptions.NetworkActionException
import ru.babaetskv.passionwoman.domain.model.CartItem
import ru.babaetskv.passionwoman.domain.usecase.base.ActionUseCase

interface CheckoutUseCase : ActionUseCase<Unit> {

    class CheckoutException(
        cause: Exception?,
        stringProvider: StringProvider
    ): NetworkActionException(stringProvider.CHECKOUT_ERROR, cause)
}
