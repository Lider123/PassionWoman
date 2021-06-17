package ru.babaetskv.passionwoman.domain.interactor

import ru.babaetskv.passionwoman.domain.gateway.CatalogGateway
import ru.babaetskv.passionwoman.domain.interactor.base.BaseUseCase
import ru.babaetskv.passionwoman.domain.interactor.exception.ErrorMessageProvider
import ru.babaetskv.passionwoman.domain.interactor.exception.NetworkDataException
import ru.babaetskv.passionwoman.domain.model.Filters
import ru.babaetskv.passionwoman.domain.model.Product
import ru.babaetskv.passionwoman.domain.model.Sorting

class GetProductsUseCase(
    private val catalogGateway: CatalogGateway,
    private val errorMessageProvider: ErrorMessageProvider
) : BaseUseCase<GetProductsUseCase.Params, List<Product>>() {

    override fun getUseCaseException(cause: Exception): Exception = GetProductsException(cause)

    override suspend fun run(params: Params): List<Product> = catalogGateway.getProducts(
        categoryId = params.categoryId,
        filters = params.filters,
        sorting = params.sorting,
        limit = params.limit,
        offset = params.offset
    )

    private inner class GetProductsException(
        cause: Exception?
    ) : NetworkDataException(errorMessageProvider.GET_PRODUCTS_ERROR, cause)

    data class Params(
        val categoryId: String?,
        val filters: Filters,
        val sorting: Sorting,
        val limit: Int,
        val offset: Int
    )
}
