package ru.babaetskv.passionwoman.domain.interactor

import ru.babaetskv.passionwoman.domain.gateway.CatalogGateway
import ru.babaetskv.passionwoman.domain.interactor.base.BaseUseCase
import ru.babaetskv.passionwoman.domain.interactor.exception.NetworkActionException
import ru.babaetskv.passionwoman.domain.interactor.exception.StringProvider
import ru.babaetskv.passionwoman.domain.preferences.FavoritesPreferences

class RemoveFromFavoritesUseCase(
    private val favoritesPreferences: FavoritesPreferences,
    private val stringProvider: StringProvider
) : BaseUseCase<String, Unit>() {

    override fun getUseCaseException(cause: Exception): Exception = RemoveFromFavoritesException(cause)

    override suspend fun run(params: String) {
        favoritesPreferences.deleteFavoriteId(params)
    }

    inner class RemoveFromFavoritesException(
        cause: Exception?
    ) : NetworkActionException(stringProvider.REMOVE_FROM_FAVORITES_ERROR, cause)
}
