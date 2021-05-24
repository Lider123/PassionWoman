package ru.babaetskv.passionwoman.domain.interactor

import ru.babaetskv.passionwoman.domain.gateway.CatalogGateway
import ru.babaetskv.passionwoman.domain.interactor.base.BaseUseCase
import ru.babaetskv.passionwoman.domain.interactor.exception.ErrorMessageProvider
import ru.babaetskv.passionwoman.domain.interactor.exception.NetworkDataException
import ru.babaetskv.passionwoman.domain.model.Product

class GetProductsUseCase(
    private val catalogGateway: CatalogGateway,
    private val errorMessageProvider: ErrorMessageProvider
) : BaseUseCase<String, List<Product>>() {

    override fun getUseCaseException(cause: Exception): Exception = GetProductsException(cause)

    override suspend fun run(params: String): List<Product> = catalogGateway.getProducts(params)

    private inner class GetProductsException(
        cause: Exception?
    ) : NetworkDataException(errorMessageProvider.GET_PRODUCTS_ERROR, cause)
}
