package ru.babaetskv.passionwoman.domain.interactor

import ru.babaetskv.passionwoman.domain.gateway.CatalogGateway
import ru.babaetskv.passionwoman.domain.interactor.base.BaseUseCase
import ru.babaetskv.passionwoman.domain.interactor.exception.ErrorMessageProvider
import ru.babaetskv.passionwoman.domain.interactor.exception.NetworkDataException
import ru.babaetskv.passionwoman.domain.model.Filters
import ru.babaetskv.passionwoman.domain.model.HomeData
import ru.babaetskv.passionwoman.domain.model.Sorting

class GetHomeDataUseCase(
    private val catalogGateway: CatalogGateway,
    private val errorMessageProvider: ErrorMessageProvider
) : BaseUseCase<Unit, HomeData>() {

    override fun getUseCaseException(cause: Exception): Exception = GetHomeDataException(cause)

    override suspend fun run(params: Unit): HomeData =
        HomeData(
            promotions = catalogGateway.getPromotions(),
            saleProducts = catalogGateway.getProducts(
                categoryId = null,
                filters = Filters.DEFAULT.copy(
                    discountOnly = true
                ),
                sorting = Sorting.DEFAULT,
                limit = PRODUCTS_LIMIT,
                offset = 0
            ),
            popularProducts = catalogGateway.getProducts(
                categoryId = null,
                filters = Filters.DEFAULT,
                sorting = Sorting.POPULARITY_DESC,
                limit = PRODUCTS_LIMIT,
                offset = 0
            ),
            newProducts = catalogGateway.getProducts(
                categoryId = null,
                filters = Filters.DEFAULT,
                sorting = Sorting.NEW_DESC,
                limit = PRODUCTS_LIMIT,
                offset = 0
            ),
            brands = catalogGateway.getBrands()
        )

    inner class GetHomeDataException(
        cause: Exception?
    ) : NetworkDataException(errorMessageProvider.GET_HOME_DATA_ERROR, cause)

    companion object {
        private const val PRODUCTS_LIMIT = 6
    }
}