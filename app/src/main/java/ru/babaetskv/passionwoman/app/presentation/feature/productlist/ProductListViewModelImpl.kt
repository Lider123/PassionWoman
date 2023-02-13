package ru.babaetskv.passionwoman.app.presentation.feature.productlist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.cachedIn
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.babaetskv.passionwoman.app.R
import ru.babaetskv.passionwoman.app.analytics.event.SelectProductEvent
import ru.babaetskv.passionwoman.app.navigation.ScreenProvider
import ru.babaetskv.passionwoman.app.presentation.base.BaseViewModel
import ru.babaetskv.passionwoman.app.presentation.base.NewPager
import ru.babaetskv.passionwoman.app.presentation.base.ViewModelDependencies
import ru.babaetskv.passionwoman.app.presentation.event.Event
import ru.babaetskv.passionwoman.domain.StringProvider
import ru.babaetskv.passionwoman.domain.model.Product
import ru.babaetskv.passionwoman.domain.model.ProductsPagedResponse
import ru.babaetskv.passionwoman.domain.model.Sorting
import ru.babaetskv.passionwoman.domain.model.filters.Filter
import ru.babaetskv.passionwoman.domain.usecase.GetProductsUseCase
import kotlin.coroutines.CoroutineContext

class ProductListViewModelImpl(
    private val args: ProductListFragment.Args,
    override val stringProvider: StringProvider,
    private val getProductsUseCase: GetProductsUseCase,
    dependencies: ViewModelDependencies
) : BaseViewModel(dependencies), ProductListViewModel {
    private val productsPager = NewPager(
        PAGE_SIZE,
        ::loadNext,
        ::onNextPageLoaded,
        ProductsPagingExceptionProvider(stringProvider)
    )
    private val searchChannel = Channel<String>(Channel.RENDEZVOUS)
    private val searchFlow = searchChannel.receiveAsFlow()
        .debounce(500L)
        .map { it.trim() }
        .onEach {
            searchQuery = it
            productsPager.invalidate()
        }
    private var pagerFilters: List<Filter> = args.filters
    private var filters: List<Filter>? = null
    private var searchQuery: String = ""
    private var totalProductsCount = 0

    override val modeLiveData = MutableLiveData(args.mode)
    override val sortingLiveData = MutableLiveData(args.sorting)
    override val appliedFiltersCountLiveData = MutableLiveData(0)
    override val productsFlow = productsPager.flow.cachedIn(viewModelScope)

    init {
        searchFlow.launchIn(this)
    }

    override fun onErrorActionPressed(exception: Exception) {
        when (exception) {
            is GetProductsUseCase -> productsPager.invalidate()
            else -> super.onErrorActionPressed(exception)
        }
    }

    override fun onEvent(event: Event) {
        when (event) {
            is ProductListViewModel.UpdateSortingEvent -> updateSorting(event.data)
            is ProductListViewModel.UpdateFiltersEvent -> updateFilters(event.data)
            else -> super.onEvent(event)
        }
    }

    override fun onProductPressed(product: Product) {
        analyticsHandler.log(SelectProductEvent(product))
        if (isPortraitModeOnly) {
            router.navigateTo(ScreenProvider.productCard(product.id))
        } else launch {
            sendEvent(ProductListViewModel.OpenLandscapeProductCard(product))
        }
    }

    override fun onBuyPressed(product: Product) {
        router.openBottomSheet(ScreenProvider.newCartItem(product))
    }

    override fun onFiltersPressed() {
        filters?.let {
            val screen = ScreenProvider.filters(
                (args.mode as? ProductListMode.Category)?.category?.id,
                it,
                totalProductsCount
            )
            router.openBottomSheet(screen)
        } ?: run {
            notifier.newRequest(this@ProductListViewModelImpl, R.string.product_list_no_filters)
                .sendAlert()
        }
    }

    override fun onSortingPressed() {
        router.openBottomSheet(ScreenProvider.sorting(sortingLiveData.value!!))
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
        launch {
            searchChannel.send(query)
        }
    }

    override fun onError(context: CoroutineContext, error: Throwable) {
        when (error) {
            is GetProductsUseCase.GetProductsPageException -> Unit
            else -> super.onError(context, error)
        }
    }

    private suspend fun loadNext(limit: Int, offset: Int): ProductsPagedResponse {
        if (args.mode is ProductListMode.Search && searchQuery.isBlank()) {
            throw GetProductsUseCase.EmptyProductsException(stringProvider)
        }

        val params = GetProductsUseCase.Params(
            categoryId = (args.mode as? ProductListMode.Category)?.category?.id,
            query = searchQuery,
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
