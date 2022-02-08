package ru.babaetskv.passionwoman.domain.interactor

import ru.babaetskv.passionwoman.domain.interactor.base.BaseUseCase
import ru.babaetskv.passionwoman.domain.interactor.exception.NetworkActionException
import ru.babaetskv.passionwoman.domain.interactor.exception.StringProvider
import ru.babaetskv.passionwoman.domain.preferences.FavoritesPreferences

class AddToFavoritesUseCase(
    private val favoritesPreferences: FavoritesPreferences,
    private val stringProvider: StringProvider
) : BaseUseCase<String, Unit>() {

    override fun getUseCaseException(cause: Exception): Exception = AddToFavoritesException(cause)

    override suspend fun run(params: String) {
        favoritesPreferences.putFavoriteId(params)
    }

    inner class AddToFavoritesException(
        cause: Exception?
    ) : NetworkActionException(stringProvider.ADD_TO_FAVORITES_ERROR, cause)
}
