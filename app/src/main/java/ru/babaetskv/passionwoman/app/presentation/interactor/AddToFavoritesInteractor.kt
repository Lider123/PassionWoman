package ru.babaetskv.passionwoman.app.presentation.interactor

import ru.babaetskv.passionwoman.app.presentation.interactor.base.BaseInteractor
import ru.babaetskv.passionwoman.domain.StringProvider
import ru.babaetskv.passionwoman.domain.exceptions.UseCaseException
import ru.babaetskv.passionwoman.domain.preferences.FavoritesPreferences
import ru.babaetskv.passionwoman.domain.usecase.AddToFavoritesUseCase

class AddToFavoritesInteractor(
    private val favoritesPreferences: FavoritesPreferences,
    private val stringProvider: StringProvider
) : BaseInteractor<String, Unit>(), AddToFavoritesUseCase {
    override val emptyException: UseCaseException.EmptyData? = null

    override fun transformException(cause: Exception): UseCaseException =
        AddToFavoritesUseCase.AddToFavoritesException(cause, stringProvider)

    override suspend fun run(params: String) {
        favoritesPreferences.putFavoriteId(params)
    }
}
