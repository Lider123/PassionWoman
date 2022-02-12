package ru.babaetskv.passionwoman.domain.usecase

import ru.babaetskv.passionwoman.domain.StringProvider
import ru.babaetskv.passionwoman.domain.exceptions.NetworkActionException
import ru.babaetskv.passionwoman.domain.model.Profile
import ru.babaetskv.passionwoman.domain.usecase.base.UseCase

interface AuthorizeUseCase : UseCase<String, Profile> {

    class AuthorizeException(
        cause: Exception?,
        stringProvider: StringProvider
    ) : NetworkActionException(stringProvider.AUTHORIZE_ERROR, cause)
}
