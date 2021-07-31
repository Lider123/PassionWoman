package ru.babaetskv.passionwoman.app.presentation.feature.productlist

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.babaetskv.passionwoman.app.R
import ru.babaetskv.passionwoman.app.presentation.base.BaseViewModel
import ru.babaetskv.passionwoman.app.presentation.base.RouterEvent
import ru.babaetskv.passionwoman.app.presentation.feature.productlist.sorting.SortingUpdateHub
import ru.babaetskv.passionwoman.app.utils.notifier.Notifier
import ru.babaetskv.passionwoman.domain.interactor.GetFavoritesUseCase
import ru.babaetskv.passionwoman.domain.interactor.GetProductUseCase
import ru.babaetskv.passionwoman.domain.interactor.GetProductsUseCase
import ru.babaetskv.passionwoman.domain.interactor.exception.StringProvider
import ru.babaetskv.passionwoman.domain.model.Filters
import ru.babaetskv.passionwoman.domain.model.Product
import ru.babaetskv.passionwoman.domain.model.Sorting
import ru.babaetskv.passionwoman.domain.preferences.FavoritesPreferences
import ru.babaetskv.passionwoman.domain.utils.execute
import kotlin.coroutines.CoroutineContext

class ProductListViewModel(
    private val args: ProductListFragment.Args,
    private val getProductsUseCase: GetProductsUseCase,
    private val getFavoritesUseCase: GetFavoritesUseCase,
    private val getProductUseCase: GetProductUseCase,
    favoritesPreferences: FavoritesPreferences,
    sortingUpdateHub: SortingUpdateHub,
    val stringProvider: StringProvider,
    notifier: Notifier
) : BaseViewModel<ProductListViewModel.Router>(notifier) {
    private val filters: Filters?
    private val sortingUpdateFlow: Flow<Sorting>?
    private val favoritesActionsFlow: Flow<FavoritesPreferences.Action>?

    val productsLiveData = MutableLiveData<List<Product>>(emptyList())
    val sortingLiveData = MutableLiveData<Sorting>()

    init {
        when (val mode = args.mode) {
            is ProductListFragment.Mode.Catalog -> {
                filters = mode.filters
                sortingUpdateFlow = sortingUpdateHub.flow.onEach(::onSortingUpdated).apply {
                    launchIn(this@ProductListViewModel)
                }
                favoritesActionsFlow = null
                sortingLiveData.postValue(mode.sorting)
            }
            ProductListFragment.Mode.Favorites -> {
                filters = null
                sortingUpdateFlow = null
                favoritesActionsFlow = favoritesPreferences.favoritesUpdatesFlow
                    .filterNotNull()
                    .onEach(::onFavoritesUpdated)
                    .apply {
                        launchIn(this@ProductListViewModel)
                    }
            }
        }
        loadProducts()
    }

    override fun onError(context: CoroutineContext, error: Throwable) {
        when (error) {
            is GetProductUseCase.GetProductException -> return
            else -> super.onError(context, error)
        }
    }

    override fun onErrorActionPressed() {
        super.onErrorActionPressed()
        loadProducts()
    }

    private fun onSortingUpdated(sorting: Sorting) {
        sortingLiveData.postValue(sorting)
        loadProducts()
    }

    private suspend fun onFavoritesUpdated(action: FavoritesPreferences.Action) {
        if (args.mode !is ProductListFragment.Mode.Favorites) return

        val currProducts = productsLiveData.value!!
        val newProducts = mutableListOf<Product>().apply {
            addAll(currProducts)
        }.let { products ->
            when (action) {
                is FavoritesPreferences.Action.Put -> {
                    if (currProducts.find { it.id == action.favoriteId } != null) return

                    val newProduct = getProductUseCase.execute(action.favoriteId)
                    products.add(0, newProduct)
                    products
                }
                is FavoritesPreferences.Action.Delete -> products.filter { it.id != action.favoriteId }
                is FavoritesPreferences.Action.Set -> {
                    loadProducts()
                    return
                }
            }
        }
        productsLiveData.postValue(newProducts)
    }

    private fun loadProducts() {
        launchWithLoading {
            val products = when (val mode = args.mode) {
                is ProductListFragment.Mode.Catalog -> {
                    // TODO: add pagination
                    getProductsUseCase.execute(GetProductsUseCase.Params(
                        categoryId = mode.categoryId,
                        filters = filters ?: Filters.DEFAULT,
                        sorting = sortingLiveData.value ?: Sorting.DEFAULT,
                        limit = 10,
                        offset = 0
                    ))
                }
                is ProductListFragment.Mode.Favorites -> getFavoritesUseCase.execute()
            }
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
