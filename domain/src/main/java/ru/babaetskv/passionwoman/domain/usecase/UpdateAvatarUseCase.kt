package ru.babaetskv.passionwoman.domain.usecase

import android.net.Uri
import ru.babaetskv.passionwoman.domain.StringProvider
import ru.babaetskv.passionwoman.domain.exceptions.NetworkActionException
import ru.babaetskv.passionwoman.domain.usecase.base.ActionUseCase

interface UpdateAvatarUseCase : ActionUseCase<Uri> {

    class UpdateAvatarException(
        cause: Exception?,
        stringProvider: StringProvider
    ) : NetworkActionException(stringProvider.UPDATE_AVATAR_ERROR, cause)
}
