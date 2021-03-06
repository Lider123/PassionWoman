package ru.babaetskv.passionwoman.app.presentation.feature.productlist

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.babaetskv.passionwoman.app.analytics.event.SelectProductEvent
import ru.babaetskv.passionwoman.app.presentation.base.BaseViewModel
import ru.babaetskv.passionwoman.app.presentation.base.ViewModelDependencies
import ru.babaetskv.passionwoman.domain.StringProvider
import ru.babaetskv.passionwoman.domain.model.Product
import ru.babaetskv.passionwoman.domain.model.Sorting
import ru.babaetskv.passionwoman.domain.preferences.FavoritesPreferences
import ru.babaetskv.passionwoman.domain.usecase.GetFavoritesUseCase
import ru.babaetskv.passionwoman.domain.usecase.GetProductUseCase
import kotlin.coroutines.CoroutineContext

class FavoritesViewModelImpl(
    private val getFavoritesUseCase: GetFavoritesUseCase,
    private val getProductUseCase: GetProductUseCase,
    favoritesPreferences: FavoritesPreferences,
    override val stringProvider: StringProvider,
    dependencies: ViewModelDependencies
) : BaseViewModel<FavoritesViewModel.Router>(dependencies), FavoritesViewModel {
    private val favoritesActionsFlow: Flow<FavoritesPreferences.Action> =
        favoritesPreferences.favoritesUpdatesFlow
            .filterNotNull()
            .onEach(::onFavoritesUpdated)

    override val productsLiveData = MutableLiveData<List<Product>>(emptyList())
    override val sortingLiveData = MutableLiveData<Sorting>()

    init {
        favoritesActionsFlow.launchIn(this@FavoritesViewModelImpl)
        loadFavorites()
    }

    override fun onError(context: CoroutineContext, error: Throwable) {
        when (error) {
            is GetProductUseCase.GetProductException -> return
            else -> super.onError(context, error)
        }
    }

    override fun onErrorActionPressed() {
        super.onErrorActionPressed()
        loadFavorites()
    }

    override fun onProductPressed(product: Product) {
        analyticsHandler.log(SelectProductEvent(product))
        launch {
            navigateTo(FavoritesViewModel.Router.ProductCardScreen(product))
        }
    }

    override fun onBuyPressed(product: Product) {
        launch {
            navigateTo(FavoritesViewModel.Router.NewCartItem(product))
        }
    }

    private suspend fun onFavoritesUpdated(action: FavoritesPreferences.Action) {
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
                    loadFavorites()
                    return
                }
            }
        }
        productsLiveData.postValue(newProducts)
    }

    private fun loadFavorites() {
        launchWithLoading {
            val products = getFavoritesUseCase.execute()
            productsLiveData.postValue(products)
        }
    }
}
