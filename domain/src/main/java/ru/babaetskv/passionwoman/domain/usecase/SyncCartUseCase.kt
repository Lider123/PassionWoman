package ru.babaetskv.passionwoman.domain.usecase

import ru.babaetskv.passionwoman.domain.StringProvider
import ru.babaetskv.passionwoman.domain.exceptions.UseCaseException
import ru.babaetskv.passionwoman.domain.usecase.base.ActionUseCase

interface SyncCartUseCase : ActionUseCase<Unit> {

    class SyncCartException(
        cause: Exception,
        stringProvider: StringProvider
    ) : UseCaseException.Data(cause, stringProvider.GET_CART_ITEMS_ERROR)
}
