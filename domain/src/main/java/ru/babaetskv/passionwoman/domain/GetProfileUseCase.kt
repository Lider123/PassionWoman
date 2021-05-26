package ru.babaetskv.passionwoman.domain

import ru.babaetskv.passionwoman.domain.interactor.base.BaseUseCase
import ru.babaetskv.passionwoman.domain.interactor.exception.ErrorMessageProvider
import ru.babaetskv.passionwoman.domain.interactor.exception.NetworkDataException
import ru.babaetskv.passionwoman.domain.model.Profile
import ru.babaetskv.passionwoman.domain.repository.AuthRepository

class GetProfileUseCase(
    private val authRepository: AuthRepository,
    private val errorMessageProvider: ErrorMessageProvider
) : BaseUseCase<Unit, Profile>() {

    override fun getUseCaseException(cause: Exception): Exception = GetProfileException(cause)

    override suspend fun run(params: Unit): Profile = authRepository.getProfile()

    inner class GetProfileException(
        cause: Exception?
    ) : NetworkDataException(errorMessageProvider.GET_PROFILE_ERROR, cause)
}
