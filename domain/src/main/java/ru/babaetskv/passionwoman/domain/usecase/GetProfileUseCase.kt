package ru.babaetskv.passionwoman.domain.usecase

import ru.babaetskv.passionwoman.domain.StringProvider
import ru.babaetskv.passionwoman.domain.exceptions.UseCaseException
import ru.babaetskv.passionwoman.domain.model.Profile
import ru.babaetskv.passionwoman.domain.usecase.base.NoParamsUseCase

interface GetProfileUseCase : NoParamsUseCase<Profile> {

    class GetProfileException(
        cause: Exception,
        stringProvider: StringProvider
    ) : UseCaseException.Data(cause, stringProvider.GET_PROFILE_ERROR)
}
