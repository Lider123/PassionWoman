package ru.babaetskv.passionwoman.domain.usecase

import ru.babaetskv.passionwoman.domain.StringProvider
import ru.babaetskv.passionwoman.domain.exceptions.UseCaseException
import ru.babaetskv.passionwoman.domain.usecase.base.ActionUseCase
import ru.babaetskv.passionwoman.domain.usecase.base.NoParamsUseCase

interface AuthorizeAsGuestUseCase : NoParamsUseCase<Unit>, ActionUseCase<Unit> {

    class AuthorizeAsGuestException(
        cause: Exception,
        stringProvider: StringProvider
    ) : UseCaseException.Action(cause, stringProvider.AUTHORIZE_AS_GUEST_ERROR)
}
