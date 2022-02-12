package ru.babaetskv.passionwoman.domain.interactor

import android.net.Uri
import ru.babaetskv.passionwoman.domain.gateway.AuthGateway
import ru.babaetskv.passionwoman.domain.interactor.base.BaseInteractor
import ru.babaetskv.passionwoman.domain.interactor.exception.StringProvider
import ru.babaetskv.passionwoman.domain.usecase.UpdateAvatarUseCase

class UpdateAvatarInteractor(
    private val authGateway: AuthGateway,
    private val stringProvider: StringProvider
) : BaseInteractor<Uri, Unit>(), UpdateAvatarUseCase {

    override fun getUseCaseException(cause: Exception): Exception =
        UpdateAvatarUseCase.UpdateAvatarException(cause, stringProvider)

    override suspend fun run(params: Uri) {
        authGateway.updateAvatar(params)
    }
}
