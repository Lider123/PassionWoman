package ru.babaetskv.passionwoman.domain.interactor

import ru.babaetskv.passionwoman.domain.gateway.CatalogGateway
import ru.babaetskv.passionwoman.domain.interactor.base.BaseUseCase
import ru.babaetskv.passionwoman.domain.interactor.exception.NetworkActionException
import ru.babaetskv.passionwoman.domain.interactor.exception.StringProvider
import ru.babaetskv.passionwoman.domain.preferences.FavoritesPreferences

typealias MergeCallback = suspend (Boolean) -> Unit

class SyncFavoritesUseCase(
    private val catalogGateway: CatalogGateway,
    private val favoritesPreferences: FavoritesPreferences,
    private val stringProvider: StringProvider
) : BaseUseCase<SyncFavoritesUseCase.Params, Unit>() {

    override fun getUseCaseException(cause: Exception): Exception = SyncFavoritesException(cause)

    override suspend fun run(params: Params) {
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

    private inner class SyncFavoritesException(
        cause: Exception
    ) : NetworkActionException(stringProvider.SYNC_FAVORITES_ERROR, cause)

    data class Params(
        val askForMerge: (doOnAnswer: MergeCallback) -> Unit,
    )
}
