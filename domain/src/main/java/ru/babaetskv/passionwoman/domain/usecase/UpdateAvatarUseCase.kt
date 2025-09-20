package ru.babaetskv.passionwoman.domain.usecase

import android.net.Uri
import ru.babaetskv.passionwoman.domain.StringProvider
import ru.babaetskv.passionwoman.domain.exceptions.UseCaseException
import ru.babaetskv.passionwoman.domain.usecase.base.ActionUseCase

interface UpdateAvatarUseCase : ActionUseCase<Uri> {

    class UpdateAvatarException(
        cause: Exception,
        stringProvider: StringProvider
    ) : UseCaseException.Action(cause, stringProvider.UPDATE_AVATAR_ERROR)
}
