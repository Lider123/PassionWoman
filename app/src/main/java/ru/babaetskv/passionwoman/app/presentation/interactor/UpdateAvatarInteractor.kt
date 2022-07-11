package ru.babaetskv.passionwoman.app.presentation.interactor

import android.net.Uri
import ru.babaetskv.passionwoman.app.presentation.interactor.base.BaseInteractor
import ru.babaetskv.passionwoman.domain.StringProvider
import ru.babaetskv.passionwoman.domain.gateway.ProfileGateway
import ru.babaetskv.passionwoman.domain.usecase.UpdateAvatarUseCase

class UpdateAvatarInteractor(
    private val profileGateway: ProfileGateway,
    private val stringProvider: StringProvider
) : BaseInteractor<Uri, Unit>(), UpdateAvatarUseCase {

    override fun getUseCaseException(cause: Exception): Exception =
        UpdateAvatarUseCase.UpdateAvatarException(cause, stringProvider)

    override suspend fun run(params: Uri) {
        profileGateway.updateAvatar(params)
    }
}
