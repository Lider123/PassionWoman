package ru.babaetskv.passionwoman.domain.usecase

import android.net.Uri
import ru.babaetskv.passionwoman.domain.interactor.exception.NetworkActionException
import ru.babaetskv.passionwoman.domain.interactor.exception.StringProvider
import ru.babaetskv.passionwoman.domain.usecase.base.ActionUseCase

interface UpdateAvatarUseCase : ActionUseCase<Uri> {

    class UpdateAvatarException(
        cause: Exception?,
        stringProvider: StringProvider
    ) : NetworkActionException(stringProvider.UPDATE_AVATAR_ERROR, cause)
}
