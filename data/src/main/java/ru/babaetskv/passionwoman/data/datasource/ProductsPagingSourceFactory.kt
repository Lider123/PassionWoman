package ru.babaetskv.passionwoman.data.datasource

import ru.babaetskv.passionwoman.data.datasource.base.InvalidatingPagingSourceFactory
import ru.babaetskv.passionwoman.domain.gateway.CatalogGateway
import ru.babaetskv.passionwoman.domain.interactor.exception.StringProvider
import ru.babaetskv.passionwoman.domain.model.Product
import ru.babaetskv.passionwoman.domain.model.Sorting
import ru.babaetskv.passionwoman.domain.model.filters.Filter

class ProductsPagingSourceFactory(
    private val catalogGateway: CatalogGateway,
    private val stringProvider: StringProvider,
    private var categoryId: String?,
    private var filters: List<Filter>,
    private var sorting: Sorting
) : InvalidatingPagingSourceFactory<Int, Product>() {
    private var filtersCallback: ((List<Filter>, Int) -> Unit)? = null

    override fun create(): ProductsDataSource =
        ProductsDataSource(catalogGateway, stringProvider, categoryId, filters, sorting).apply {
            setOnFiltersLoaded(filtersCallback)
        }

    fun updateFilters(filters: List<Filter>) {
        this.filters = filters
        invalidate()
    }

    fun updateSorting(sorting: Sorting) {
        this.sorting = sorting
        invalidate()
    }

    fun setOnProductsDataUpdated(callback: (List<Filter>, Int) -> Unit) {
        filtersCallback = callback
    }
}
