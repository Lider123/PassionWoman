package ru.babaetskv.passionwoman.domain.interactor

import ru.babaetskv.passionwoman.domain.interactor.base.BaseUseCase
import ru.babaetskv.passionwoman.domain.interactor.exception.StringProvider
import ru.babaetskv.passionwoman.domain.interactor.exception.NetworkActionException
import ru.babaetskv.passionwoman.domain.preferences.AuthPreferences
import ru.babaetskv.passionwoman.domain.preferences.FavoritesPreferences

class LogOutUseCase(
    private val authPreferences: AuthPreferences,
    private val favoritesPreferences: FavoritesPreferences,
    private val stringProvider: StringProvider
) : BaseUseCase<Unit, Unit>() {

    override fun getUseCaseException(cause: Exception): Exception = LogOutException(cause)

    override suspend fun run(params: Unit) {
        authPreferences.reset()
        favoritesPreferences.clear()
    }

    inner class LogOutException(cause: Exception?) : NetworkActionException(stringProvider.LOG_OUT_ERROR, cause)
}
