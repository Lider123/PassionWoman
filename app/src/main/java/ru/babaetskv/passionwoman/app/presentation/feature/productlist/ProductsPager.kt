package ru.babaetskv.passionwoman.app.presentation.feature.productlist

import ru.babaetskv.passionwoman.app.presentation.base.SimplePager
import ru.babaetskv.passionwoman.data.datasource.ProductsPagingSourceFactory
import ru.babaetskv.passionwoman.domain.model.Product
import ru.babaetskv.passionwoman.domain.model.Sorting
import ru.babaetskv.passionwoman.domain.model.filters.Filter

class ProductsPager(
    pageSize: Int,
    pagingSourceFactory: ProductsPagingSourceFactory
) : SimplePager<Int, Product>(pageSize, pagingSourceFactory) {

    fun updateFilters(filters: List<Filter>) {
        with (pagingSourceFactory as ProductsPagingSourceFactory) {
            updateFilters(filters)
        }
    }

    fun updateSorting(sorting: Sorting) {
        with (pagingSourceFactory as ProductsPagingSourceFactory) {
            updateSorting(sorting)
        }
    }
}
