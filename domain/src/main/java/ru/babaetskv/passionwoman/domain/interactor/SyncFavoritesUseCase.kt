package ru.babaetskv.passionwoman.domain.interactor

import ru.babaetskv.passionwoman.domain.gateway.CatalogGateway
import ru.babaetskv.passionwoman.domain.interactor.base.BaseUseCase
import ru.babaetskv.passionwoman.domain.interactor.exception.NetworkDataException
import ru.babaetskv.passionwoman.domain.interactor.exception.StringProvider

class SyncFavoritesUseCase(
    private val catalogGateway: CatalogGateway,
    private val stringProvider: StringProvider
) : BaseUseCase<Boolean, Unit>() {

    override fun getUseCaseException(cause: Exception): Exception = SyncFavoritesException(cause)

    override suspend fun run(params: Boolean) {
        catalogGateway.syncFavorites(params)
    }

    private inner class SyncFavoritesException(
        cause: Exception?
    ) : NetworkDataException(stringProvider.SYNC_FAVORITES_ERROR, cause)
}
