package ru.babaetskv.passionwoman.domain.usecase

import ru.babaetskv.passionwoman.domain.StringProvider
import ru.babaetskv.passionwoman.domain.exceptions.UseCaseException
import ru.babaetskv.passionwoman.domain.usecase.base.ActionUseCase
import ru.babaetskv.passionwoman.domain.usecase.base.UseCase

interface CheckoutUseCase : UseCase<Unit, Long> {

    class CheckoutException(
        cause: Exception,
        stringProvider: StringProvider
    ): UseCaseException.Action(cause, stringProvider.CHECKOUT_ERROR)
}
