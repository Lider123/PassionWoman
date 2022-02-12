package ru.babaetskv.passionwoman.domain.usecase

import ru.babaetskv.passionwoman.domain.StringProvider
import ru.babaetskv.passionwoman.domain.exceptions.NetworkActionException
import ru.babaetskv.passionwoman.domain.usecase.base.ActionUseCase
import ru.babaetskv.passionwoman.domain.usecase.base.NoParamsUseCase

interface LogOutUseCase : ActionUseCase<Unit>, NoParamsUseCase<Unit> {

    class LogOutException(
        cause: Exception?,
        stringProvider: StringProvider
    ) : NetworkActionException(stringProvider.LOG_OUT_ERROR, cause)
}
