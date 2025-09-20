package ru.babaetskv.passionwoman.app.presentation.interactor

import ru.babaetskv.passionwoman.domain.gateway.AuthGateway
import ru.babaetskv.passionwoman.app.presentation.interactor.base.BaseInteractor
import ru.babaetskv.passionwoman.domain.StringProvider
import ru.babaetskv.passionwoman.domain.exceptions.UseCaseException
import ru.babaetskv.passionwoman.domain.gateway.ProfileGateway
import ru.babaetskv.passionwoman.domain.model.Profile
import ru.babaetskv.passionwoman.domain.model.base.Transformable.Companion.transform
import ru.babaetskv.passionwoman.domain.preferences.AuthPreferences
import ru.babaetskv.passionwoman.domain.usecase.AuthorizeUseCase
import timber.log.Timber

class AuthorizeInteractor(
    private val authGateway: AuthGateway,
    private val profileGateway: ProfileGateway,
    private val authPreferences: AuthPreferences,
    private val stringProvider: StringProvider
) : BaseInteractor<String, Profile>(), AuthorizeUseCase {
    override val emptyException: UseCaseException.EmptyData? = null

    override fun transformException(cause: Exception): UseCaseException =
        AuthorizeUseCase.AuthorizeException(cause, stringProvider)

    override suspend fun run(params: String): Profile {
        Timber.e("run(params=$params)") // TODO: remove
        val authToken = authGateway.authorize(params)
        Timber.e("authToken=$authToken") // TODO: remove
        authPreferences.authToken = authToken
        authPreferences.authType = AuthPreferences.AuthType.AUTHORIZED
        return profileGateway.getProfile().transform().also {
            authPreferences.userId = it.id
        }
    }
}
