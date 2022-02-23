package ru.babaetskv.passionwoman.app.presentation.interactor

import ru.babaetskv.passionwoman.domain.gateway.CatalogGateway
import ru.babaetskv.passionwoman.app.presentation.interactor.base.BaseInteractor
import ru.babaetskv.passionwoman.domain.StringProvider
import ru.babaetskv.passionwoman.domain.model.ProductsPagedResponse
import ru.babaetskv.passionwoman.domain.usecase.GetProductsUseCase
import timber.log.Timber

class GetProductsInteractor(
    private val catalogGateway: CatalogGateway,
    private val stringProvider: StringProvider
) : BaseInteractor<GetProductsUseCase.Params, ProductsPagedResponse>(), GetProductsUseCase {

    override suspend fun run(params: GetProductsUseCase.Params): ProductsPagedResponse =
        catalogGateway.getProducts(
            categoryId = params.categoryId,
            limit = params.limit,
            offset = params.offset,
            filters = params.filters,
            sorting = params.sorting
        ).transform(stringProvider).also {
            Timber.e("Request: $params\nResponse: $it") // TODO: remove
        }

    override fun getUseCaseException(cause: Exception): Exception =
        GetProductsUseCase.GetProductsException(cause, stringProvider)
}
