package ru.babaetskv.passionwoman.app.presentation.interactor

import ru.babaetskv.passionwoman.domain.gateway.CatalogGateway
import ru.babaetskv.passionwoman.app.presentation.interactor.base.BaseInteractor
import ru.babaetskv.passionwoman.domain.StringProvider
import ru.babaetskv.passionwoman.domain.model.Product
import ru.babaetskv.passionwoman.domain.preferences.FavoritesPreferences
import ru.babaetskv.passionwoman.domain.usecase.GetFavoritesUseCase
import ru.babaetskv.passionwoman.domain.utils.transformList

class GetFavoritesInteractor(
    private val catalogGateway: CatalogGateway,
    private val favoritesPreferences: FavoritesPreferences,
    private val stringProvider: StringProvider
) : BaseInteractor<Unit, List<Product>>(), GetFavoritesUseCase {

    override fun getUseCaseException(cause: Exception): Exception =
        GetFavoritesUseCase.GetFavoritesException(cause, stringProvider)

    override suspend fun run(params: Unit): List<Product> =
        catalogGateway.getFavorites(favoritesPreferences.getFavoriteIds())
            .transformList()
            .also {
                if (it.isEmpty()) throw GetFavoritesUseCase.EmptyFavoritesException(stringProvider)
            }
}
