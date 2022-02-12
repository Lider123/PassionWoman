package ru.babaetskv.passionwoman.domain.interactor

import ru.babaetskv.passionwoman.domain.gateway.AuthGateway
import ru.babaetskv.passionwoman.domain.interactor.base.BaseInteractor
import ru.babaetskv.passionwoman.domain.interactor.exception.StringProvider
import ru.babaetskv.passionwoman.domain.model.Profile
import ru.babaetskv.passionwoman.domain.preferences.AuthPreferences
import ru.babaetskv.passionwoman.domain.usecase.AuthorizeUseCase
import ru.babaetskv.passionwoman.domain.utils.transform

class AuthorizeInteractor(
    private val authGateway: AuthGateway,
    private val authPreferences: AuthPreferences,
    private val stringProvider: StringProvider
) : BaseInteractor<String, Profile>(), AuthorizeUseCase {

    override fun getUseCaseException(cause: Exception): Exception =
        AuthorizeUseCase.AuthorizeException(cause, stringProvider)

    override suspend fun run(params: String): Profile {
        val authToken = authGateway.authorize(params)
        authPreferences.authToken = authToken
        authPreferences.authType = AuthPreferences.AuthType.AUTHORIZED
        return authGateway.getProfile().transform().also {
            authPreferences.userId = it.id
        }
    }
}
