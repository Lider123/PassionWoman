package ru.babaetskv.passionwoman.app.presentation.interactor

import android.content.res.Resources
import ru.babaetskv.passionwoman.app.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
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
    private val stringProvider: StringProvider,
    private val resources: Resources
) : BaseInteractor<Unit, HomeData>(), GetHomeDataUseCase {

    override fun getUseCaseException(cause: Exception): Exception =
        GetHomeDataUseCase.GetHomeDataException(cause, stringProvider)

    override suspend fun run(params: Unit): HomeData = coroutineScope {
        val brandsCount = 2 * resources.getInteger(R.integer.brands_list_span_count)
        val promotionsAsync = async(Dispatchers.IO) {
            catalogGateway.getPromotions().transformList()
        }
        val storiesAsync = async(Dispatchers.IO) {
            catalogGateway.getStories().transformList()
        }
        val saleProductsAsync = async(Dispatchers.IO) {
            catalogGateway.getProducts(
                categoryId = null,
                query = "",
                filters = listOf(
                    Filter.DiscountOnly(stringProvider, true)
                ),
                sorting = Sorting.DEFAULT,
                limit = PRODUCTS_LIMIT,
                offset = 0
            ).transform(stringProvider)
        }
        val popularProductsAsync = async(Dispatchers.IO) {
            catalogGateway.getProducts(
                categoryId = null,
                query = "",
                filters = listOf(),
                sorting = Sorting.POPULARITY,
                limit = PRODUCTS_LIMIT,
                offset = 0
            ).transform(stringProvider)
        }
        val newProductsAsync = async(Dispatchers.IO) {
            catalogGateway.getProducts(
                categoryId = null,
                query = "",
                filters = listOf(),
                sorting = Sorting.NEW,
                limit = PRODUCTS_LIMIT,
                offset = 0
            ).transform(stringProvider)
        }
        val brandsAsync = async(Dispatchers.IO) {
            catalogGateway.getPopularBrands(brandsCount).transformList()
        }
        return@coroutineScope HomeData(
            promotions = promotionsAsync.await(),
            stories = storiesAsync.await(),
            saleProducts = saleProductsAsync.await(),
            popularProducts = popularProductsAsync.await(),
            newProducts = newProductsAsync.await(),
            brands = brandsAsync.await()
        )
    }

    companion object {
        private const val PRODUCTS_LIMIT = 6
    }
}
