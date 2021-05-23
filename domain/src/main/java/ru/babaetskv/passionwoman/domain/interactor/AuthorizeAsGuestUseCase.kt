package ru.babaetskv.passionwoman.domain.interactor

import ru.babaetskv.passionwoman.domain.interactor.base.BaseUseCase
import ru.babaetskv.passionwoman.domain.interactor.exception.ErrorMessageProvider
import ru.babaetskv.passionwoman.domain.interactor.exception.NetworkActionException
import ru.babaetskv.passionwoman.domain.preferences.AuthPreferences

class AuthorizeAsGuestUseCase(
    private val authPreferences: AuthPreferences,
    private val errorMessageProvider: ErrorMessageProvider
) : BaseUseCase<Unit, Unit>() {

    override fun getUseCaseException(cause: Exception): Exception = AuthorizeAsGuestException(cause)

    override suspend fun run(params: Unit) {
        authPreferences.authToken = ""
        authPreferences.authType = AuthPreferences.AuthType.GUEST
    }

    inner class AuthorizeAsGuestException(
        cause: Exception?
    ) : NetworkActionException(errorMessageProvider.AUTHORIZE_AS_GUEST_ERROR, cause)
}
