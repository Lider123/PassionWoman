package ru.babaetskv.passionwoman.domain.interactor

import android.net.Uri
import ru.babaetskv.passionwoman.domain.gateway.AuthGateway
import ru.babaetskv.passionwoman.domain.interactor.base.BaseUseCase
import ru.babaetskv.passionwoman.domain.interactor.exception.StringProvider
import ru.babaetskv.passionwoman.domain.interactor.exception.NetworkActionException

class UpdateAvatarUseCase(
    private val authGateway: AuthGateway,
    private val stringProvider: StringProvider
) : BaseUseCase<Uri, Unit>() {

    override fun getUseCaseException(cause: Exception): Exception = UpdateAvatarException(cause)

    override suspend fun run(params: Uri) {
        authGateway.updateAvatar(params)
    }

    inner class UpdateAvatarException(
        cause: Exception?
    ) : NetworkActionException(stringProvider.UPDATE_AVATAR_ERROR, cause)
}
