package ru.babaetskv.passionwoman.domain.interactor

import ru.babaetskv.passionwoman.domain.gateway.CatalogGateway
import ru.babaetskv.passionwoman.domain.interactor.base.BaseUseCase
import ru.babaetskv.passionwoman.domain.interactor.exception.NetworkDataException
import ru.babaetskv.passionwoman.domain.interactor.exception.StringProvider
import ru.babaetskv.passionwoman.domain.model.Product

class GetFavoritesUseCase(
    private val catalogGateway: CatalogGateway,
    private val stringProvider: StringProvider
) : BaseUseCase<Unit, List<Product>>() {

    override fun getUseCaseException(cause: Exception): Exception = GetFavoritesException(cause)

    override suspend fun run(params: Unit): List<Product> = catalogGateway.getFavorites()

    private inner class GetFavoritesException(
        cause: Exception?
    ) : NetworkDataException(stringProvider.GET_FAVORITES_ERROR, cause)
}