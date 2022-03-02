package ru.babaetskv.passionwoman.app.presentation.interactor

import ru.babaetskv.passionwoman.domain.gateway.CatalogGateway
import ru.babaetskv.passionwoman.app.presentation.interactor.base.BaseInteractor
import ru.babaetskv.passionwoman.domain.StringProvider
import ru.babaetskv.passionwoman.domain.model.HomeData
import ru.babaetskv.passionwoman.domain.model.Sorting
import ru.babaetskv.passionwoman.domain.model.filters.Filter
import ru.babaetskv.passionwoman.domain.usecase.GetHomeDataUseCase
import ru.babaetskv.passionwoman.domain.utils.transformList

class GetHomeDataInteractor(
    private val catalogGateway: CatalogGateway,
    private val stringProvider: StringProvider
) : BaseInteractor<Unit, HomeData>(), GetHomeDataUseCase {

    override fun getUseCaseException(cause: Exception): Exception =
        GetHomeDataUseCase.GetHomeDataException(cause, stringProvider)

    override suspend fun run(params: Unit): HomeData =
        HomeData(
            promotions = catalogGateway.getPromotions().transformList(),
            stories = catalogGateway.getStories().transformList(),
            saleProducts = catalogGateway.getProducts(
                categoryId = null,
                query = "",
                filters = listOf(
                    Filter.DiscountOnly(stringProvider, true)
                ),
                sorting = Sorting.DEFAULT,
                limit = PRODUCTS_LIMIT,
                offset = 0
            ).transform(stringProvider),
            popularProducts = catalogGateway.getProducts(
                categoryId = null,
                query = "",
                filters = listOf(),
                sorting = Sorting.POPULARITY,
                limit = PRODUCTS_LIMIT,
                offset = 0
            ).transform(stringProvider),
            newProducts = catalogGateway.getProducts(
                categoryId = null,
                query = "",
                filters = listOf(),
                sorting = Sorting.NEW,
                limit = PRODUCTS_LIMIT,
                offset = 0
            ).transform(stringProvider),
            brands = catalogGateway.getPopularBrands().transformList()
        )

    companion object {
        private const val PRODUCTS_LIMIT = 6
    }
}
