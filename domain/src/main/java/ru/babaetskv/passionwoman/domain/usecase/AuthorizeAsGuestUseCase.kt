package ru.babaetskv.passionwoman.domain.usecase

import ru.babaetskv.passionwoman.domain.interactor.exception.NetworkActionException
import ru.babaetskv.passionwoman.domain.interactor.exception.StringProvider
import ru.babaetskv.passionwoman.domain.usecase.base.ActionUseCase
import ru.babaetskv.passionwoman.domain.usecase.base.NoParamsUseCase

interface AuthorizeAsGuestUseCase : NoParamsUseCase<Unit>, ActionUseCase<Unit> {

    class AuthorizeAsGuestException(
        cause: Exception?,
        stringProvider: StringProvider
    ) : NetworkActionException(stringProvider.AUTHORIZE_AS_GUEST_ERROR, cause)
}
