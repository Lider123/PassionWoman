package ru.babaetskv.passionwoman.app.presentation.interactor

import ru.babaetskv.passionwoman.domain.gateway.AuthGateway
import ru.babaetskv.passionwoman.app.presentation.interactor.base.BaseInteractor
import ru.babaetskv.passionwoman.domain.StringProvider
import ru.babaetskv.passionwoman.domain.model.Profile
import ru.babaetskv.passionwoman.domain.preferences.AuthPreferences
import ru.babaetskv.passionwoman.domain.usecase.UpdateProfileUseCase

class UpdateProfileInteractor(
    private val authGateway: AuthGateway,
    private val authPreferences: AuthPreferences,
    private val stringProvider: StringProvider
) : BaseInteractor<Profile, Unit>(), UpdateProfileUseCase {

    override fun getUseCaseException(cause: Exception): Exception =
        UpdateProfileUseCase.UpdateProfileException(cause, stringProvider)

    override suspend fun run(params: Profile) {
        authGateway.updateProfile(params)
        authPreferences.profileIsFilled = true
    }
}
