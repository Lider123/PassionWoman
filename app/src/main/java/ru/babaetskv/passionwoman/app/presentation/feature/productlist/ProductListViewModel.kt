package ru.babaetskv.passionwoman.app.presentation.feature.productlist

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.babaetskv.passionwoman.app.R
import ru.babaetskv.passionwoman.app.presentation.base.BaseViewModel
import ru.babaetskv.passionwoman.app.presentation.base.RouterEvent
import ru.babaetskv.passionwoman.app.presentation.feature.productlist.sorting.SortingUpdateHub
import ru.babaetskv.passionwoman.app.utils.notifier.Notifier
import ru.babaetskv.passionwoman.domain.interactor.GetProductsUseCase
import ru.babaetskv.passionwoman.domain.interactor.exception.StringProvider
import ru.babaetskv.passionwoman.domain.model.Product
import ru.babaetskv.passionwoman.domain.model.Sorting

class ProductListViewModel(
    private val args: ProductListFragment.Args,
    private val getProductsUseCase: GetProductsUseCase,
    sortingUpdateHub: SortingUpdateHub,
    val stringProvider: StringProvider,
    notifier: Notifier
) : BaseViewModel<ProductListViewModel.Router>(notifier) {
    private val filters = args.filters
    private val sortingUpdateFlow = sortingUpdateHub.flow.onEach(::onSortingUpdated)

    val productsLiveData = MutableLiveData<List<Product>>(emptyList())
    val sortingLiveData = MutableLiveData(args.sorting)

    init {
        sortingUpdateFlow.launchIn(this)
        loadProducts()
    }

    override fun onErrorActionPressed() {
        super.onErrorActionPressed()
        loadProducts()
    }

    private fun onSortingUpdated(sorting: Sorting) {
        sortingLiveData.postValue(sorting)
        loadProducts()
    }

    private fun loadProducts() {
        launchWithLoading {
            // TODO: add pagination
            val products = getProductsUseCase.execute(GetProductsUseCase.Params(
                categoryId = args.categoryId,
                filters = filters,
                sorting = sortingLiveData.value!!,
                limit = 10,
                offset = 0
            ))
            // TODO: handle empty products list
            productsLiveData.postValue(products)
        }
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
            // TODO: open in bottomsheet
            navigateTo(Router.SortingScreen(sortingLiveData.value!!))
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
}
