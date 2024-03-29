package ru.babaetskv.passionwoman.app.presentation.interactor

import ru.babaetskv.passionwoman.domain.gateway.CatalogGateway
import ru.babaetskv.passionwoman.app.presentation.interactor.base.BaseInteractor
import ru.babaetskv.passionwoman.domain.StringProvider
import ru.babaetskv.passionwoman.domain.exceptions.UseCaseException
import ru.babaetskv.passionwoman.domain.model.ProductsPagedResponse
import ru.babaetskv.passionwoman.domain.usecase.GetProductsUseCase

class GetProductsInteractor(
    private val catalogGateway: CatalogGateway,
    private val stringProvider: StringProvider
) : BaseInteractor<GetProductsUseCase.Params, ProductsPagedResponse>(), GetProductsUseCase {
    override val emptyException: UseCaseException.EmptyData? = null

    override fun transformException(cause: Exception): UseCaseException =
        GetProductsUseCase.GetProductsPageException(cause, stringProvider)

    override suspend fun run(params: GetProductsUseCase.Params): ProductsPagedResponse =
        catalogGateway.getProducts(
            categoryId = params.categoryId,
            query = params.query,
            limit = params.limit,
            offset = params.offset,
            filters = params.filters,
            sorting = params.sorting
        ).transform(stringProvider)
}
