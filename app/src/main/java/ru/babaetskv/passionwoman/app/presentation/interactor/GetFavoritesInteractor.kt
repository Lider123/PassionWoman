package ru.babaetskv.passionwoman.app.presentation.interactor

import ru.babaetskv.passionwoman.domain.gateway.CatalogGateway
import ru.babaetskv.passionwoman.app.presentation.interactor.base.BaseInteractor
import ru.babaetskv.passionwoman.domain.StringProvider
import ru.babaetskv.passionwoman.domain.exceptions.UseCaseException
import ru.babaetskv.passionwoman.domain.model.Product
import ru.babaetskv.passionwoman.domain.model.base.Transformable.Companion.transformList
import ru.babaetskv.passionwoman.domain.preferences.FavoritesPreferences
import ru.babaetskv.passionwoman.domain.usecase.GetFavoritesUseCase

class GetFavoritesInteractor(
    private val catalogGateway: CatalogGateway,
    private val favoritesPreferences: FavoritesPreferences,
    private val stringProvider: StringProvider
) : BaseInteractor<Unit, List<Product>>(), GetFavoritesUseCase {
    override val emptyException: UseCaseException.EmptyData =
        GetFavoritesUseCase.EmptyFavoritesException(stringProvider)

    override fun transformException(cause: Exception): UseCaseException =
        GetFavoritesUseCase.GetFavoritesException(cause, stringProvider)

    override suspend fun run(params: Unit): List<Product> =
        catalogGateway.getFavorites(favoritesPreferences.getFavoriteIds()).transformList()
}
