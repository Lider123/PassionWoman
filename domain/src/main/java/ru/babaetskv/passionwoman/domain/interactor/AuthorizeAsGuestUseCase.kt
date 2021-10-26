package ru.babaetskv.passionwoman.domain.interactor

import ru.babaetskv.passionwoman.domain.interactor.base.BaseUseCase
import ru.babaetskv.passionwoman.domain.interactor.exception.StringProvider
import ru.babaetskv.passionwoman.domain.interactor.exception.NetworkActionException
import ru.babaetskv.passionwoman.domain.preferences.AuthPreferences

class AuthorizeAsGuestUseCase(
    private val authPreferences: AuthPreferences,
    private val stringProvider: StringProvider
) : BaseUseCase<Unit, Unit>() {

    override fun getUseCaseException(cause: Exception): Exception = AuthorizeAsGuestException(cause)

    override suspend fun run(params: Unit) {
        authPreferences.authToken = ""
        authPreferences.userId = "guest"
        authPreferences.authType = AuthPreferences.AuthType.GUEST
    }

    inner class AuthorizeAsGuestException(
        cause: Exception?
    ) : NetworkActionException(stringProvider.AUTHORIZE_AS_GUEST_ERROR, cause)
}
