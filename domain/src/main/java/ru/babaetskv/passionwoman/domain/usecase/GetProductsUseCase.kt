package ru.babaetskv.passionwoman.domain.usecase

import ru.babaetskv.passionwoman.domain.StringProvider
import ru.babaetskv.passionwoman.domain.exceptions.NetworkDataException
import ru.babaetskv.passionwoman.domain.model.ProductsPagedResponse
import ru.babaetskv.passionwoman.domain.model.Sorting
import ru.babaetskv.passionwoman.domain.model.filters.Filter
import ru.babaetskv.passionwoman.domain.usecase.base.UseCase

interface GetProductsUseCase : UseCase<GetProductsUseCase.Params, ProductsPagedResponse> {

    data class Params(
        val categoryId: String?,
        val limit: Int,
        val offset: Int,
        val filters: List<Filter>,
        val sorting: Sorting
    )

    class GetProductsException(
        cause: Exception?,
        stringProvider: StringProvider
    ) : NetworkDataException(stringProvider.GET_PRODUCTS_ERROR, cause, dataIsOptional = true)
}
