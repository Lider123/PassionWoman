package ru.babaetskv.passionwoman.domain.interactor

import ru.babaetskv.passionwoman.domain.gateway.CatalogGateway
import ru.babaetskv.passionwoman.domain.interactor.base.BaseUseCase
import ru.babaetskv.passionwoman.domain.interactor.exception.NetworkDataException
import ru.babaetskv.passionwoman.domain.interactor.exception.StringProvider
import ru.babaetskv.passionwoman.domain.model.Product

class GetProductUseCase(
    private val catalogGateway: CatalogGateway,
    private val stringProvider: StringProvider
) : BaseUseCase<String, Product>() {

    override fun getUseCaseException(cause: Exception): Exception = GetProductException(cause)

    override suspend fun run(params: String): Product = catalogGateway.getProduct(params)

    inner class GetProductException(
        cause: Exception?
    ) : NetworkDataException(stringProvider.GET_PRODUCT_ERROR, cause)
}
