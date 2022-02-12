package ru.babaetskv.passionwoman.domain.interactor

import ru.babaetskv.passionwoman.domain.interactor.base.BaseInteractor
import ru.babaetskv.passionwoman.domain.interactor.exception.StringProvider
import ru.babaetskv.passionwoman.domain.preferences.FavoritesPreferences
import ru.babaetskv.passionwoman.domain.usecase.AddToFavoritesUseCase

class AddToFavoritesInteractor(
    private val favoritesPreferences: FavoritesPreferences,
    private val stringProvider: StringProvider
) : BaseInteractor<String, Unit>(), AddToFavoritesUseCase {

    override fun getUseCaseException(cause: Exception): Exception =
        AddToFavoritesUseCase.AddToFavoritesException(cause, stringProvider)

    override suspend fun run(params: String) {
        favoritesPreferences.putFavoriteId(params)
    }
}
