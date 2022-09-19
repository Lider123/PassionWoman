package ru.babaetskv.passionwoman.app.presentation.interactor

import android.content.res.Resources
import ru.babaetskv.passionwoman.app.R
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import ru.babaetskv.passionwoman.domain.gateway.CatalogGateway
import ru.babaetskv.passionwoman.app.presentation.interactor.base.BaseInteractor
import ru.babaetskv.passionwoman.domain.AppDispatchers
import ru.babaetskv.passionwoman.domain.StringProvider
import ru.babaetskv.passionwoman.domain.exceptions.UseCaseException
import ru.babaetskv.passionwoman.domain.model.HomeData
import ru.babaetskv.passionwoman.domain.model.Sorting
import ru.babaetskv.passionwoman.domain.model.base.Transformable.Companion.transformList
import ru.babaetskv.passionwoman.domain.model.filters.Filter
import ru.babaetskv.passionwoman.domain.usecase.GetHomeDataUseCase

class GetHomeDataInteractor(
    private val catalogGateway: CatalogGateway,
    private val stringProvider: StringProvider,
    private val dispatchers: AppDispatchers,
    private val resources: Resources
) : BaseInteractor<Unit, HomeData>(), GetHomeDataUseCase {
    override val emptyException: UseCaseException.EmptyData? = null

    override fun transformException(cause: Exception): UseCaseException =
        GetHomeDataUseCase.GetHomeDataException(cause, stringProvider)

    override suspend fun run(params: Unit): HomeData = coroutineScope {
        val brandsCount = 2 * resources.getInteger(R.integer.brands_list_span_count)
        val promotionsAsync = async(dispatchers.Default) {
            catalogGateway.getPromotions().transformList()
        }
        val storiesAsync = async(dispatchers.Default) {
            catalogGateway.getStories().transformList()
        }
        val saleProductsAsync = async(dispatchers.Default) {
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
        val popularProductsAsync = async(dispatchers.Default) {
            catalogGateway.getProducts(
                categoryId = null,
                query = "",
                filters = listOf(),
                sorting = Sorting.POPULARITY,
                limit = PRODUCTS_LIMIT,
                offset = 0
            ).transform(stringProvider)
        }
        val newProductsAsync = async(dispatchers.Default) {
            catalogGateway.getProducts(
                categoryId = null,
                query = "",
                filters = listOf(),
                sorting = Sorting.NEW,
                limit = PRODUCTS_LIMIT,
                offset = 0
            ).transform(stringProvider)
        }
        val brandsAsync = async(dispatchers.Default) {
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
