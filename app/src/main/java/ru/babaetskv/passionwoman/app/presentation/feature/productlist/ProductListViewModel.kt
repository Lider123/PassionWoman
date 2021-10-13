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
import ru.babaetskv.passionwoman.app.presentation.base.BaseViewModel
import ru.babaetskv.passionwoman.app.presentation.base.RouterEvent
import ru.babaetskv.passionwoman.app.presentation.base.SimplePager
import ru.babaetskv.passionwoman.app.presentation.feature.productlist.sorting.SortingUpdateHub
import ru.babaetskv.passionwoman.app.utils.notifier.Notifier
import ru.babaetskv.passionwoman.data.datasource.ProductsDataSource
import ru.babaetskv.passionwoman.domain.interactor.exception.StringProvider
import ru.babaetskv.passionwoman.domain.model.Product
import ru.babaetskv.passionwoman.domain.model.Sorting

class ProductListViewModel(
    args: ProductListFragment.Args,
    sortingUpdateHub: SortingUpdateHub,
    val stringProvider: StringProvider,
    notifier: Notifier,
    productsDataSource: ProductsDataSource
) : BaseViewModel<ProductListViewModel.Router>(notifier) {
    private val sortingUpdateFlow: Flow<Sorting> = sortingUpdateHub.flow
        .onEach(::onSortingUpdated)
    private val productsPager = SimplePager(PAGE_SIZE) { productsDataSource }

    val sortingLiveData = MutableLiveData(args.sorting)
    val productsFlow = productsPager.flow.cachedIn(viewModelScope)

    init {
        sortingUpdateFlow.launchIn(this@ProductListViewModel)
    }

    override fun onErrorActionPressed() {
        super.onErrorActionPressed()
        productsPager.invalidate()
    }

    private fun onSortingUpdated(sorting: Sorting) {
        sortingLiveData.postValue(sorting)
        productsPager.invalidate()
    }

    fun onProductPressed(product: Product) {
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
        // TODO
        notifier.newRequest(this, R.string.in_development)
            .sendError()
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
    }

    companion object {
        private const val PAGE_SIZE = 10
    }
}
