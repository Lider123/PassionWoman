package ru.babaetskv.passionwoman.domain.interactor

import ru.babaetskv.passionwoman.domain.interactor.base.BaseUseCase
import ru.babaetskv.passionwoman.domain.interactor.exception.ErrorMessageProvider
import ru.babaetskv.passionwoman.domain.interactor.exception.NetworkActionException
import ru.babaetskv.passionwoman.domain.model.Profile
import ru.babaetskv.passionwoman.domain.preferences.AuthPreferences
import ru.babaetskv.passionwoman.domain.repository.AuthRepository

class AuthorizeUseCase(
    private val authRepository: AuthRepository,
    private val authPreferences: AuthPreferences,
    private val errorMessageProvider: ErrorMessageProvider
) : BaseUseCase<String, Profile>() {

    override fun getUseCaseException(cause: Exception): Exception = AuthorizeException(cause)

    override suspend fun run(params: String): Profile {
        val authToken = authRepository.authorize(params)
        authPreferences.authToken = authToken
        authPreferences.authType = AuthPreferences.AuthType.AUTHORIZED
        return authRepository.getProfile()
    }

    inner class AuthorizeException(
        cause: Exception?
    ) : NetworkActionException(errorMessageProvider.AUTHORIZE_ERROR, cause)
}
