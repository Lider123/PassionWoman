package ru.babaetskv.passionwoman.data.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import ru.babaetskv.passionwoman.domain.gateway.CatalogGateway
import ru.babaetskv.passionwoman.domain.interactor.exception.EmptyDataException
import ru.babaetskv.passionwoman.domain.interactor.exception.NetworkDataException
import ru.babaetskv.passionwoman.domain.interactor.exception.StringProvider
import ru.babaetskv.passionwoman.domain.model.Product
import ru.babaetskv.passionwoman.domain.model.Sorting
import ru.babaetskv.passionwoman.domain.model.filters.Filter

class ProductsDataSource(
    private val catalogGateway: CatalogGateway,
    private val stringProvider: StringProvider,
    private val categoryId: String?,
    private val filters: List<Filter>,
    private val sorting: Sorting
) : PagingSource<Int, Product>() {
    private var filtersCallback: ((List<Filter>, Int) -> Unit)? = null

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Product> {
        val currentPage = params.key ?: START_PAGE
        return try {
            val limit = params.loadSize
            val offset = limit * (currentPage - 1)
            val response = catalogGateway.getProducts(
                categoryId = categoryId,
                limit = limit,
                offset = offset,
                filters = filters,
                sorting = sorting,
            ).transform(stringProvider)
            val products = response.products
            if (products.isEmpty() && currentPage == START_PAGE) throw EmptyProductsException()

            filtersCallback?.invoke(response.availableFilters, response.total)
            LoadResult.Page(
                data = products,
                prevKey = if (currentPage == START_PAGE) null else currentPage - 1,
                nextKey = if (products.isNotEmpty()) currentPage + 1 else null
            )
        } catch (e: Exception) {
            LoadResult.Error(if (currentPage == START_PAGE) GetProductsException(e) else GetProductsPageException(e))
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Product>): Int? = state.anchorPosition?.let {
        state.closestPageToPosition(it)?.prevKey?.plus(1)
            ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
    }

    fun setOnFiltersLoaded(callback: ((List<Filter>, Int) -> Unit)?) {
        filtersCallback = callback
    }

    inner class GetProductsException(
        cause: Exception?
    ) : NetworkDataException(stringProvider.GET_PRODUCTS_ERROR, cause)

    inner class GetProductsPageException(
        cause: Exception?
    ) : NetworkDataException(stringProvider.GET_PRODUCTS_PAGE_ERROR, cause, dataIsOptional = true)

    inner class EmptyProductsException : EmptyDataException(stringProvider.EMPTY_PRODUCTS_ERROR)

    companion object {
        private const val START_PAGE = 1
    }
}
