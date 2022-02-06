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
import ru.babaetskv.passionwoman.app.presentation.base.ViewModelDependencies
import ru.babaetskv.passionwoman.app.presentation.event.InnerEvent
import ru.babaetskv.passionwoman.data.datasource.ProductsPagingSourceFactory
import ru.babaetskv.passionwoman.domain.interactor.exception.StringProvider
import ru.babaetskv.passionwoman.domain.model.Product
import ru.babaetskv.passionwoman.domain.model.Sorting
import ru.babaetskv.passionwoman.domain.model.filters.Filter

class ProductListViewModelImpl(
    args: ProductListFragment.Args,
    override val stringProvider: StringProvider,
    productsPagingSourceFactory: ProductsPagingSourceFactory,
    dependencies: ViewModelDependencies
) : BaseViewModel<ProductListViewModel.Router>(dependencies), ProductListViewModel {
    private val productsPager = ProductsPager(PAGE_SIZE, productsPagingSourceFactory.also {
        it.setOnProductsDataUpdated(::onProductsDataUpdated)
    })
    private var filters: List<Filter>? = null
    private var totalProductsCount = 0

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

    private fun updateSorting(sorting: Sorting) {
        sortingLiveData.postValue(sorting)
        productsPager.updateSorting(sorting)
    }

    private fun updateFilters(filters: List<Filter>) {
        this.filters = filters
        productsPager.updateFilters(filters)
        appliedFiltersCountLiveData.postValue(filters.count { it.isSelected })
    }

    private fun onProductsDataUpdated(availableFilters: List<Filter>, totalCount: Int) {
        if (filters == null) {
            filters = availableFilters
        }
        totalProductsCount = totalCount
    }

    companion object {
        private const val PAGE_SIZE = 10
    }
}
