package ru.babaetskv.passionwoman.app.presentation.interactor

import ru.babaetskv.passionwoman.app.presentation.interactor.base.BaseInteractor
import ru.babaetskv.passionwoman.domain.StringProvider
import ru.babaetskv.passionwoman.domain.preferences.AuthPreferences
import ru.babaetskv.passionwoman.domain.preferences.FavoritesPreferences
import ru.babaetskv.passionwoman.domain.usecase.LogOutUseCase

class LogOutInteractor(
    private val authPreferences: AuthPreferences,
    private val favoritesPreferences: FavoritesPreferences,
    private val stringProvider: StringProvider
) : BaseInteractor<Unit, Unit>(), LogOutUseCase {

    override fun getUseCaseException(cause: Exception): Exception =
        LogOutUseCase.LogOutException(cause, stringProvider)

    override suspend fun run(params: Unit) {
        authPreferences.reset()
        favoritesPreferences.reset()
    }
}
