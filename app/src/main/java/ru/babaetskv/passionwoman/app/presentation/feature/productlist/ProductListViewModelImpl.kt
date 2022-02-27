package ru.babaetskv.passionwoman.app.presentation.feature.productlist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.cachedIn
import kotlinx.coroutines.launch
import ru.babaetskv.passionwoman.app.R
import ru.babaetskv.passionwoman.app.analytics.event.SelectProductEvent
import ru.babaetskv.passionwoman.app.presentation.base.BaseViewModel
import ru.babaetskv.passionwoman.app.presentation.base.NewPager
import ru.babaetskv.passionwoman.app.presentation.base.ViewModelDependencies
import ru.babaetskv.passionwoman.app.presentation.event.InnerEvent
import ru.babaetskv.passionwoman.domain.StringProvider
import ru.babaetskv.passionwoman.domain.exceptions.EmptyDataException
import ru.babaetskv.passionwoman.domain.exceptions.NetworkDataException
import ru.babaetskv.passionwoman.domain.model.Product
import ru.babaetskv.passionwoman.domain.model.ProductsPagedResponse
import ru.babaetskv.passionwoman.domain.model.Sorting
import ru.babaetskv.passionwoman.domain.model.filters.Filter
import ru.babaetskv.passionwoman.domain.usecase.GetProductsUseCase

class ProductListViewModelImpl(
    private val args: ProductListFragment.Args,
    override val stringProvider: StringProvider,
    private val getProductsUseCase: GetProductsUseCase,
    dependencies: ViewModelDependencies
) : BaseViewModel<ProductListViewModel.Router>(dependencies), ProductListViewModel {
    private val pagingExceptionProvider = object : NewPager.PagingExceptionProvider {
        override val emptyError: EmptyDataException
            get() = GetProductsUseCase.EmptyProductsException(stringProvider)

        override fun getListError(cause: Exception): NetworkDataException =
            GetProductsUseCase.GetProductsException(cause, stringProvider)

        override fun getPageError(cause: Exception): NetworkDataException =
            GetProductsUseCase.GetProductsPageException(cause, stringProvider)
    }
    private val productsPager = NewPager(
        PAGE_SIZE,
        ::loadNext,
        ::onNextPageLoaded,
        pagingExceptionProvider
    )
    private var pagerFilters: List<Filter> = args.filters
    private var filters: List<Filter>? = null
    private var totalProductsCount = 0

    override val modeLiveData = MutableLiveData(args.mode)
    override val sortingLiveData = MutableLiveData(args.sorting)
    override val appliedFiltersCountLiveData = MutableLiveData(0)
    override val productsFlow = productsPager.flow.cachedIn(viewModelScope)

    override fun onErrorActionPressed() {
        super.onErrorActionPressed()
        productsPager.invalidate()
    }

    override fun onEvent(event: InnerEvent) {
        when (event) {
            is InnerEvent.UpdateSorting -> updateSorting(event.data)
            is InnerEvent.UpdateFilters -> updateFilters(event.data)
            else -> super.onEvent(event)
        }
    }

    override fun onProductPressed(product: Product) {
        analyticsHandler.log(SelectProductEvent(product))
        launch {
            navigateTo(ProductListViewModel.Router.ProductCardScreen(product))
        }
    }

    override fun onBuyPressed(product: Product) {
        // TODO
        notifier.newRequest(this, R.string.in_development)
            .sendError()
    }

    override fun onFiltersPressed() {
        filters?.let {
            launch {
                navigateTo(ProductListViewModel.Router.FiltersScreen(it, totalProductsCount))
            }
        } ?: run {
            notifier.newRequest(this@ProductListViewModelImpl, R.string.product_list_no_filters)
                .sendAlert()
        }
    }

    override fun onSortingPressed() {
        launch {
            navigateTo(ProductListViewModel.Router.SortingScreen(sortingLiveData.value!!))
        }
    }

    override fun onLoadStateChanged(states: CombinedLoadStates) {
        when (val state = states.refresh) {
            is LoadState.NotLoading -> {
                loadingLiveData.postValue(false)
                errorLiveData.postValue(null)
            }
            LoadState.Loading -> {
                loadingLiveData.postValue(true)
                errorLiveData.postValue(null)
            }
            is LoadState.Error -> onError(coroutineContext, state.error)
        }
    }

    override fun onSearchQueryChanged(query: String) {
        // TODO
    }

    private suspend fun loadNext(limit: Int, offset: Int): ProductsPagedResponse {
        val params = GetProductsUseCase.Params(
            categoryId = (args.mode as? ProductListMode.CategoryMode)?.category?.id,
            filters = pagerFilters,
            sorting = sortingLiveData.value!!,
            limit = limit,
            offset = offset
        )
        return getProductsUseCase.execute(params)
    }

    private fun onNextPageLoaded(data: ProductsPagedResponse) {
        if (filters == null) {
            filters = data.availableFilters
        }
        totalProductsCount = data.total
    }

    private fun updateSorting(sorting: Sorting) {
        sortingLiveData.postValue(sorting)
        productsPager.invalidate()
    }

    private fun updateFilters(filters: List<Filter>) {
        this.filters = filters
        pagerFilters = filters
        productsPager.invalidate()
        appliedFiltersCountLiveData.postValue(filters.count { it.isSelected })
    }

    companion object {
        private const val PAGE_SIZE = 10
    }
}
