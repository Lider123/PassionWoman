package ru.babaetskv.passionwoman.domain.usecase

import ru.babaetskv.passionwoman.domain.StringProvider
import ru.babaetskv.passionwoman.domain.exceptions.UseCaseException
import ru.babaetskv.passionwoman.domain.usecase.base.ActionUseCase

interface CheckoutUseCase : ActionUseCase<Unit> {

    class CheckoutException(
        cause: Exception,
        stringProvider: StringProvider
    ): UseCaseException.Action(cause, stringProvider.CHECKOUT_ERROR)
}
