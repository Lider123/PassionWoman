package ru.babaetskv.passionwoman.domain.interactor

import ru.babaetskv.passionwoman.domain.gateway.CatalogGateway
import ru.babaetskv.passionwoman.domain.interactor.base.BaseUseCase
import ru.babaetskv.passionwoman.domain.interactor.exception.StringProvider
import ru.babaetskv.passionwoman.domain.interactor.exception.NetworkDataException
import ru.babaetskv.passionwoman.domain.model.HomeData
import ru.babaetskv.passionwoman.domain.model.Sorting
import ru.babaetskv.passionwoman.domain.model.filters.Filter
import ru.babaetskv.passionwoman.domain.utils.transformList

class GetHomeDataUseCase(
    private val catalogGateway: CatalogGateway,
    private val stringProvider: StringProvider
) : BaseUseCase<Unit, HomeData>() {

    override fun getUseCaseException(cause: Exception): Exception = GetHomeDataException(cause)

    override suspend fun run(params: Unit): HomeData =
        HomeData(
            promotions = catalogGateway.getPromotions().transformList(),
            stories = catalogGateway.getStories().transformList(),
            saleProducts = catalogGateway.getProducts(
                categoryId = null,
                filters = listOf(
                    Filter.DiscountOnly(stringProvider, true)
                ),
                sorting = Sorting.DEFAULT,
                limit = PRODUCTS_LIMIT,
                offset = 0
            ).transform(stringProvider).products,
            popularProducts = catalogGateway.getProducts(
                categoryId = null,
                filters = listOf(),
                sorting = Sorting.POPULARITY,
                limit = PRODUCTS_LIMIT,
                offset = 0
            ).transform(stringProvider).products,
            newProducts = catalogGateway.getProducts(
                categoryId = null,
                filters = listOf(),
                sorting = Sorting.NEW,
                limit = PRODUCTS_LIMIT,
                offset = 0
            ).transform(stringProvider).products,
            brands = catalogGateway.getPopularBrands().transformList()
        )

    inner class GetHomeDataException(
        cause: Exception?
    ) : NetworkDataException(stringProvider.GET_HOME_DATA_ERROR, cause)

    companion object {
        private const val PRODUCTS_LIMIT = 6
    }
}