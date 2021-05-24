package ru.babaetskv.passionwoman.domain.interactor

import ru.babaetskv.passionwoman.domain.interactor.base.BaseUseCase
import ru.babaetskv.passionwoman.domain.interactor.exception.ErrorMessageProvider
import ru.babaetskv.passionwoman.domain.interactor.exception.NetworkActionException
import ru.babaetskv.passionwoman.domain.preferences.AuthPreferences

class LogOutUseCase(
    private val authPreferences: AuthPreferences,
    private val errorMessageProvider: ErrorMessageProvider
) : BaseUseCase<Unit, Unit>() {

    override fun getUseCaseException(cause: Exception): Exception = LogOutException(cause)

    override suspend fun run(params: Unit) {
        authPreferences.reset()
    }

    inner class LogOutException(cause: Exception?) : NetworkActionException(errorMessageProvider.LOG_OUT_ERROR, cause)
}
