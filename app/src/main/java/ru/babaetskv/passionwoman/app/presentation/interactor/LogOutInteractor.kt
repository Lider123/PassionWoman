package ru.babaetskv.passionwoman.app.presentation.interactor

import ru.babaetskv.passionwoman.app.presentation.interactor.base.BaseInteractor
import ru.babaetskv.passionwoman.domain.StringProvider
import ru.babaetskv.passionwoman.domain.dataflow.CartFlow
import ru.babaetskv.passionwoman.domain.exceptions.UseCaseException
import ru.babaetskv.passionwoman.domain.preferences.AuthPreferences
import ru.babaetskv.passionwoman.domain.preferences.FavoritesPreferences
import ru.babaetskv.passionwoman.domain.usecase.LogOutUseCase

class LogOutInteractor(
    private val authPreferences: AuthPreferences,
    private val favoritesPreferences: FavoritesPreferences,
    private val cartFlow: CartFlow,
    private val stringProvider: StringProvider
) : BaseInteractor<Unit, Unit>(), LogOutUseCase {
    override val emptyException: UseCaseException.EmptyData? = null

    override fun transformException(cause: Exception): UseCaseException =
        LogOutUseCase.LogOutException(cause, stringProvider)

    override suspend fun run(params: Unit) {
        authPreferences.reset()
        favoritesPreferences.reset()
        cartFlow.clear()
    }
}
