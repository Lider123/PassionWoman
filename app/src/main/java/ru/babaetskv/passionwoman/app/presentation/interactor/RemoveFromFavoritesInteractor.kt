package ru.babaetskv.passionwoman.app.presentation.interactor

import ru.babaetskv.passionwoman.app.presentation.interactor.base.BaseInteractor
import ru.babaetskv.passionwoman.domain.StringProvider
import ru.babaetskv.passionwoman.domain.exceptions.UseCaseException
import ru.babaetskv.passionwoman.domain.preferences.FavoritesPreferences
import ru.babaetskv.passionwoman.domain.usecase.RemoveFromFavoritesUseCase

class RemoveFromFavoritesInteractor(
    private val favoritesPreferences: FavoritesPreferences,
    private val stringProvider: StringProvider
) : BaseInteractor<Int, Unit>(), RemoveFromFavoritesUseCase {
    override val emptyException: UseCaseException.EmptyData? = null

    override fun transformException(cause: Exception): UseCaseException =
        RemoveFromFavoritesUseCase.RemoveFromFavoritesException(cause, stringProvider)

    override suspend fun run(params: Int) {
        favoritesPreferences.deleteFavoriteId(params)
    }
}
