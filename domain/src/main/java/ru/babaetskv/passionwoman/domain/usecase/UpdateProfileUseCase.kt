package ru.babaetskv.passionwoman.domain.usecase

import ru.babaetskv.passionwoman.domain.StringProvider
import ru.babaetskv.passionwoman.domain.exceptions.NetworkActionException
import ru.babaetskv.passionwoman.domain.model.Profile
import ru.babaetskv.passionwoman.domain.usecase.base.ActionUseCase

interface UpdateProfileUseCase : ActionUseCase<Profile> {

    class UpdateProfileException(
        cause: Exception,
        stringProvider: StringProvider
    ) : NetworkActionException(stringProvider.UPDATE_PROFILE_ERROR, cause)
}
