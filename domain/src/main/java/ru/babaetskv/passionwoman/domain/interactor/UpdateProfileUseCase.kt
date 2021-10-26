package ru.babaetskv.passionwoman.domain.interactor

import ru.babaetskv.passionwoman.domain.gateway.AuthGateway
import ru.babaetskv.passionwoman.domain.interactor.base.BaseUseCase
import ru.babaetskv.passionwoman.domain.interactor.exception.StringProvider
import ru.babaetskv.passionwoman.domain.interactor.exception.NetworkActionException
import ru.babaetskv.passionwoman.domain.model.Profile
import ru.babaetskv.passionwoman.domain.preferences.AuthPreferences

class UpdateProfileUseCase(
    private val authGateway: AuthGateway,
    private val authPreferences: AuthPreferences,
    private val stringProvider: StringProvider
) : BaseUseCase<Profile, Unit>() {

    override fun getUseCaseException(cause: Exception): Exception = UpdateProfileException(cause)

    override suspend fun run(params: Profile) {
        authGateway.updateProfile(params)
        authPreferences.profileIsFilled = true
    }

    inner class UpdateProfileException(
        cause: Exception
    ) : NetworkActionException(stringProvider.UPDATE_PROFILE_ERROR, cause)
}
