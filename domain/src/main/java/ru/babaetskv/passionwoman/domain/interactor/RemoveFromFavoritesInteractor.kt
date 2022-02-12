package ru.babaetskv.passionwoman.domain.interactor

import ru.babaetskv.passionwoman.domain.interactor.base.BaseInteractor
import ru.babaetskv.passionwoman.domain.interactor.exception.StringProvider
import ru.babaetskv.passionwoman.domain.preferences.FavoritesPreferences
import ru.babaetskv.passionwoman.domain.usecase.RemoveFromFavoritesUseCase

class RemoveFromFavoritesInteractor(
    private val favoritesPreferences: FavoritesPreferences,
    private val stringProvider: StringProvider
) : BaseInteractor<String, Unit>(), RemoveFromFavoritesUseCase {

    override fun getUseCaseException(cause: Exception): Exception =
        RemoveFromFavoritesUseCase.RemoveFromFavoritesException(cause, stringProvider)

    override suspend fun run(params: String) {
        favoritesPreferences.deleteFavoriteId(params)
    }
}
