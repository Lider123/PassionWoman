package ru.babaetskv.passionwoman.domain.usecase

import ru.babaetskv.passionwoman.domain.interactor.exception.NetworkDataException
import ru.babaetskv.passionwoman.domain.interactor.exception.StringProvider
import ru.babaetskv.passionwoman.domain.model.Profile
import ru.babaetskv.passionwoman.domain.usecase.base.NoParamsUseCase

interface GetProfileUseCase : NoParamsUseCase<Profile> {

    class GetProfileException(
        cause: Exception?,
        stringProvider: StringProvider
    ) : NetworkDataException(stringProvider.GET_PROFILE_ERROR, cause)
}
