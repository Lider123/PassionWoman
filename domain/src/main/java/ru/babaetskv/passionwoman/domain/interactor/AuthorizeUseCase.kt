package ru.babaetskv.passionwoman.domain.interactor

import ru.babaetskv.passionwoman.domain.gateway.AuthGateway
import ru.babaetskv.passionwoman.domain.interactor.base.BaseUseCase
import ru.babaetskv.passionwoman.domain.interactor.exception.StringProvider
import ru.babaetskv.passionwoman.domain.interactor.exception.NetworkActionException
import ru.babaetskv.passionwoman.domain.model.Profile
import ru.babaetskv.passionwoman.domain.preferences.AuthPreferences

class AuthorizeUseCase(
    private val authGateway: AuthGateway,
    private val authPreferences: AuthPreferences,
    private val stringProvider: StringProvider
) : BaseUseCase<String, Profile>() {

    override fun getUseCaseException(cause: Exception): Exception = AuthorizeException(cause)

    override suspend fun run(params: String): Profile {
        val authToken = authGateway.authorize(params)
        authPreferences.authToken = authToken
        authPreferences.authType = AuthPreferences.AuthType.AUTHORIZED
        return authGateway.getProfile().also {
            authPreferences.userId = it.id
        }
    }

    inner class AuthorizeException(
        cause: Exception?
    ) : NetworkActionException(stringProvider.AUTHORIZE_ERROR, cause)
}
