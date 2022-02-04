package ru.babaetskv.passionwoman.domain.interactor

import ru.babaetskv.passionwoman.domain.gateway.CatalogGateway
import ru.babaetskv.passionwoman.domain.interactor.base.BaseUseCase
import ru.babaetskv.passionwoman.domain.interactor.exception.StringProvider
import ru.babaetskv.passionwoman.domain.interactor.exception.NetworkDataException
import ru.babaetskv.passionwoman.domain.model.HomeData
import ru.babaetskv.passionwoman.domain.model.Sorting
import ru.babaetskv.passionwoman.domain.model.filters.Filter

class GetHomeDataUseCase(
    private val catalogGateway: CatalogGateway,
    private val stringProvider: StringProvider
) : BaseUseCase<Unit, HomeData>() {

    override fun getUseCaseException(cause: Exception): Exception = GetHomeDataException(cause)

    override suspend fun run(params: Unit): HomeData =
        HomeData(
            promotions = catalogGateway.getPromotions(),
            stories = catalogGateway.getStories(),
            saleProducts = catalogGateway.getProducts(
                categoryId = null,
                filters = listOf(
                    Filter.DiscountOnly(stringProvider, true)
                ),
                sorting = Sorting.DEFAULT,
                limit = PRODUCTS_LIMIT,
                offset = 0
            ).products,
            popularProducts = catalogGateway.getProducts(
                categoryId = null,
                filters = listOf(),
                sorting = Sorting.POPULARITY,
                limit = PRODUCTS_LIMIT,
                offset = 0
            ).products,
            newProducts = catalogGateway.getProducts(
                categoryId = null,
                filters = listOf(),
                sorting = Sorting.NEW,
                limit = PRODUCTS_LIMIT,
                offset = 0
            ).products,
            brands = catalogGateway.getPopularBrands()
        )

    inner class GetHomeDataException(
        cause: Exception?
    ) : NetworkDataException(stringProvider.GET_HOME_DATA_ERROR, cause)

    companion object {
        private const val PRODUCTS_LIMIT = 6
    }
}