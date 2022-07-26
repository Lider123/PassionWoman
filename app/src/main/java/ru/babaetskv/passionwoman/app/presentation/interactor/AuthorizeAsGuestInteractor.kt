package ru.babaetskv.passionwoman.app.presentation.interactor

import ru.babaetskv.passionwoman.app.presentation.interactor.base.BaseInteractor
import ru.babaetskv.passionwoman.domain.StringProvider
import ru.babaetskv.passionwoman.domain.exceptions.UseCaseException
import ru.babaetskv.passionwoman.domain.preferences.AuthPreferences
import ru.babaetskv.passionwoman.domain.usecase.AuthorizeAsGuestUseCase

class AuthorizeAsGuestInteractor(
    private val authPreferences: AuthPreferences,
    private val stringProvider: StringProvider
) : BaseInteractor<Unit, Unit>(), AuthorizeAsGuestUseCase {
    override val emptyException: UseCaseException.EmptyData? = null

    override fun transformException(cause: Exception): UseCaseException =
        AuthorizeAsGuestUseCase.AuthorizeAsGuestException(cause, stringProvider)

    override suspend fun run(params: Unit) {
        authPreferences.authToken = ""
        authPreferences.userId = "guest"
        authPreferences.authType = AuthPreferences.AuthType.GUEST
    }
}
