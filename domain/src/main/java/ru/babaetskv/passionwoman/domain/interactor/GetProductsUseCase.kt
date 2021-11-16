package ru.babaetskv.passionwoman.domain.interactor

import ru.babaetskv.passionwoman.domain.gateway.CatalogGateway
import ru.babaetskv.passionwoman.domain.interactor.base.BaseUseCase
import ru.babaetskv.passionwoman.domain.interactor.exception.NetworkDataException
import ru.babaetskv.passionwoman.domain.interactor.exception.StringProvider
import ru.babaetskv.passionwoman.domain.model.ProductsPagedResponse
import ru.babaetskv.passionwoman.domain.model.Sorting
import ru.babaetskv.passionwoman.domain.model.filters.Filter

class GetProductsUseCase(
    private val catalogGateway: CatalogGateway,
    private val stringProvider: StringProvider
) : BaseUseCase<GetProductsUseCase.Params, ProductsPagedResponse>() {

    override suspend fun run(params: Params): ProductsPagedResponse =
        catalogGateway.getProducts(
            categoryId = params.categoryId,
            limit = params.limit,
            offset = params.offset,
            filters = params.filters,
            sorting = params.sorting
        )

    override fun getUseCaseException(cause: Exception): Exception = GetProductsException(cause)

    data class Params(
        val categoryId: String?,
        val limit: Int,
        val offset: Int,
        val filters: List<Filter>,
        val sorting: Sorting
    )

    inner class GetProductsException(
        cause: Exception?
    ) : NetworkDataException(stringProvider.GET_PRODUCTS_ERROR, cause, dataIsOptional = true)
}
