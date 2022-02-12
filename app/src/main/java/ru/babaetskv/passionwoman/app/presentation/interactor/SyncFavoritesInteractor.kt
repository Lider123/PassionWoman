package ru.babaetskv.passionwoman.app.presentation.interactor

import ru.babaetskv.passionwoman.domain.gateway.CatalogGateway
import ru.babaetskv.passionwoman.app.presentation.interactor.base.BaseInteractor
import ru.babaetskv.passionwoman.domain.StringProvider
import ru.babaetskv.passionwoman.domain.preferences.FavoritesPreferences
import ru.babaetskv.passionwoman.domain.usecase.SyncFavoritesUseCase

class SyncFavoritesInteractor(
    private val catalogGateway: CatalogGateway,
    private val favoritesPreferences: FavoritesPreferences,
    private val stringProvider: StringProvider
) : BaseInteractor<SyncFavoritesUseCase.Params, Unit>(), SyncFavoritesUseCase {

    override fun getUseCaseException(cause: Exception): Exception =
        SyncFavoritesUseCase.SyncFavoritesException(cause, stringProvider)

    override suspend fun run(params: SyncFavoritesUseCase.Params) {
        val favorites: MutableSet<String> = catalogGateway.getFavoriteIds().toMutableSet()
        val oldFavorites = favoritesPreferences.getFavoriteIds()
        if (!favorites.containsAll(oldFavorites) || !oldFavorites.containsAll(favorites)) {
            if (oldFavorites.isNotEmpty()) {
                params.askForMerge.invoke { mergeIsNeeded ->
                    favoritesPreferences.setFavoriteIds(favorites.apply {
                        if (mergeIsNeeded) {
                            addAll(oldFavorites)
                            catalogGateway.setFavoriteIds(this.toList())
                        }
                    })
                }
            } else favoritesPreferences.setFavoriteIds(favorites)
        }
    }
}
