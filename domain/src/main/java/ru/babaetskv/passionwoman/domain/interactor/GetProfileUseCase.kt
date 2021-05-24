package ru.babaetskv.passionwoman.domain.interactor

import ru.babaetskv.passionwoman.domain.gateway.AuthGateway
import ru.babaetskv.passionwoman.domain.interactor.base.BaseUseCase
import ru.babaetskv.passionwoman.domain.interactor.exception.ErrorMessageProvider
import ru.babaetskv.passionwoman.domain.interactor.exception.NetworkDataException
import ru.babaetskv.passionwoman.domain.model.Profile

class GetProfileUseCase(
    private val authGateway: AuthGateway,
    private val errorMessageProvider: ErrorMessageProvider
) : BaseUseCase<Unit, Profile>() {

    override fun getUseCaseException(cause: Exception): Exception = GetProfileException(cause)

    override suspend fun run(params: Unit): Profile = authGateway.getProfile()

    private inner class GetProfileException(
        cause: Exception?
    ) : NetworkDataException(errorMessageProvider.GET_PROFILE_ERROR, cause)
}
