package ru.babaetskv.passionwoman.app.presentation.interactor

import android.net.Uri
import ru.babaetskv.passionwoman.app.presentation.interactor.base.BaseInteractor
import ru.babaetskv.passionwoman.domain.StringProvider
import ru.babaetskv.passionwoman.domain.exceptions.UseCaseException
import ru.babaetskv.passionwoman.domain.gateway.ProfileGateway
import ru.babaetskv.passionwoman.domain.usecase.UpdateAvatarUseCase

class UpdateAvatarInteractor(
    private val profileGateway: ProfileGateway,
    private val stringProvider: StringProvider
) : BaseInteractor<Uri, Unit>(), UpdateAvatarUseCase {
    override val emptyException: UseCaseException.EmptyData? = null

    override fun transformException(cause: Exception): UseCaseException =
        UpdateAvatarUseCase.UpdateAvatarException(cause, stringProvider)

    override suspend fun run(params: Uri) {
        profileGateway.updateAvatar(params)
    }
}
