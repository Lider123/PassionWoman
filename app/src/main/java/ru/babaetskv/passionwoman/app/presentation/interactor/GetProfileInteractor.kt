package ru.babaetskv.passionwoman.app.presentation.interactor

import ru.babaetskv.passionwoman.domain.gateway.AuthGateway
import ru.babaetskv.passionwoman.app.presentation.interactor.base.BaseInteractor
import ru.babaetskv.passionwoman.domain.StringProvider
import ru.babaetskv.passionwoman.domain.model.Profile
import ru.babaetskv.passionwoman.domain.usecase.GetProfileUseCase
import ru.babaetskv.passionwoman.domain.utils.transform

class GetProfileInteractor(
    private val authGateway: AuthGateway,
    private val stringProvider: StringProvider
) : BaseInteractor<Unit, Profile>(), GetProfileUseCase {

    override fun getUseCaseException(cause: Exception): Exception =
        GetProfileUseCase.GetProfileException(cause, stringProvider)

    override suspend fun run(params: Unit): Profile = authGateway.getProfile().transform()
}
