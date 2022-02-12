package ru.babaetskv.passionwoman.domain.usecase

import ru.babaetskv.passionwoman.domain.interactor.exception.NetworkActionException
import ru.babaetskv.passionwoman.domain.interactor.exception.StringProvider
import ru.babaetskv.passionwoman.domain.model.Profile
import ru.babaetskv.passionwoman.domain.usecase.base.ActionUseCase

interface UpdateProfileUseCase : ActionUseCase<Profile> {

    class UpdateProfileException(
        cause: Exception,
        stringProvider: StringProvider
    ) : NetworkActionException(stringProvider.UPDATE_PROFILE_ERROR, cause)
}
