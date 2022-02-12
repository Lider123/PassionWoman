package ru.babaetskv.passionwoman.domain.usecase

import ru.babaetskv.passionwoman.domain.interactor.exception.NetworkActionException
import ru.babaetskv.passionwoman.domain.interactor.exception.StringProvider
import ru.babaetskv.passionwoman.domain.usecase.base.ActionUseCase
import ru.babaetskv.passionwoman.domain.usecase.base.NoParamsUseCase

interface LogOutUseCase : ActionUseCase<Unit>, NoParamsUseCase<Unit> {

    class LogOutException(
        cause: Exception?,
        stringProvider: StringProvider
    ) : NetworkActionException(stringProvider.LOG_OUT_ERROR, cause)
}
