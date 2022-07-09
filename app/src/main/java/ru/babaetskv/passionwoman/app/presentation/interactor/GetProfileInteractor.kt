package ru.babaetskv.passionwoman.app.presentation.interactor

import ru.babaetskv.passionwoman.app.presentation.interactor.base.BaseInteractor
import ru.babaetskv.passionwoman.domain.StringProvider
import ru.babaetskv.passionwoman.domain.gateway.ProfileGateway
import ru.babaetskv.passionwoman.domain.model.Profile
import ru.babaetskv.passionwoman.domain.usecase.GetProfileUseCase
import ru.babaetskv.passionwoman.domain.utils.transform

class GetProfileInteractor(
    private val profileGateway: ProfileGateway,
    private val stringProvider: StringProvider
) : BaseInteractor<Unit, Profile>(), GetProfileUseCase {

    override fun getUseCaseException(cause: Exception): Exception =
        GetProfileUseCase.GetProfileException(cause, stringProvider)

    override suspend fun run(params: Unit): Profile = profileGateway.getProfile().transform()
}
