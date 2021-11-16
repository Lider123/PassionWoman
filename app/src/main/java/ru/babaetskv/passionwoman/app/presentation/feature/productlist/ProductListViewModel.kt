package ru.babaetskv.passionwoman.app.presentation.feature.productlist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.babaetskv.passionwoman.app.R
import ru.babaetskv.passionwoman.app.analytics.event.SelectProductEvent
import ru.babaetskv.passionwoman.app.presentation.base.BaseViewModel
import ru.babaetskv.passionwoman.app.presentation.base.RouterEvent
import ru.babaetskv.passionwoman.app.presentation.base.ViewModelDependencies
import ru.babaetskv.passionwoman.app.presentation.feature.productlist.filters.FiltersUpdateHub
import ru.babaetskv.passionwoman.app.presentation.feature.productlist.sorting.SortingUpdateHub
import ru.babaetskv.passionwoman.data.datasource.ProductsPagingSourceFactory
import ru.babaetskv.passionwoman.domain.interactor.exception.StringProvider
import ru.babaetskv.passionwoman.domain.model.Product
import ru.babaetskv.passionwoman.domain.model.Sorting
import ru.babaetskv.passionwoman.domain.model.filters.Filter

class ProductListViewModel(
    args: ProductListFragment.Args,
    sortingUpdateHub: SortingUpdateHub,
    filtersUpdateHub: FiltersUpdateHub,
    val stringProvider: StringProvider,
    productsPagingSourceFactory: ProductsPagingSourceFactory,
    dependencies: ViewModelDependencies
) : BaseViewModel<ProductListViewModel.Router>(dependencies) {
    private val sortingUpdateFlow: Flow<Sorting> = sortingUpdateHub.flow
        .onEach(::onSortingUpdated)
    private val filtersUpdateFlow: Flow<List<Filter>> = filtersUpdateHub.flow
        .onEach(::onFiltersUpdated)
    private val productsPager = ProductsPager(PAGE_SIZE, productsPagingSourceFactory.also {
        it.setOnProductsDataUpdated(::onProductsDataUpdated)
    })
    private var filters: List<Filter>? = null
    private var totalProductsCount = 0

    val sortingLiveData = MutableLiveData(args.sorting)
    val appliedFiltersCountLiveData = MutableLiveData(0)
    val productsFlow = productsPager.flow.cachedIn(viewModelScope)

    init {
        sortingUpdateFlow.launchIn(this)
        filtersUpdateFlow.launchIn(this)
    }

    override fun onErrorActionPressed() {
        super.onErrorActionPressed()
        productsPager.invalidate()
    }

    private fun onSortingUpdated(sorting: Sorting) {
        sortingLiveData.postValue(sorting)
        productsPager.updateSorting(sorting)
    }

    private fun onFiltersUpdated(filters: List<Filter>) {
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

    fun onProductPressed(product: Product) {
        analyticsHandler.log(SelectProductEvent(product))
        launch {
            navigateTo(Router.ProductCardScreen(product))
        }
    }

    fun onBuyPressed(product: Product) {
        // TODO
        notifier.newRequest(this, R.string.in_development)
            .sendError()
    }

    fun onFiltersPressed() {
        filters?.let {
            launch {
                navigateTo(Router.FiltersScreen(it, totalProductsCount))
            }
        } ?: run {
            notifier.newRequest(this@ProductListViewModel, R.string.product_list_no_filters)
                .sendAlert()
        }
    }

    fun onSortingPressed() {
        launch {
            navigateTo(Router.SortingScreen(sortingLiveData.value!!))
        }
    }

    fun onLoadStateChanged(states: CombinedLoadStates) {
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

    sealed class Router : RouterEvent {

        data class ProductCardScreen(
            val product: Product
        ) : Router()

        data class SortingScreen(
            val selectedSorting: Sorting
        ) : Router()

        data class FiltersScreen(
            val filters: List<Filter>,
            val productsCount: Int
        ) : Router()
    }

    companion object {
        private const val PAGE_SIZE = 10
    }
}
