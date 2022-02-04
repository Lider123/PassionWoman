package ru.babaetskv.passionwoman.app.presentation.feature.productlist

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.babaetskv.passionwoman.app.R
import ru.babaetskv.passionwoman.app.analytics.event.SelectProductEvent
import ru.babaetskv.passionwoman.app.presentation.base.BaseViewModel
import ru.babaetskv.passionwoman.app.presentation.base.RouterEvent
import ru.babaetskv.passionwoman.app.presentation.base.ViewModelDependencies
import ru.babaetskv.passionwoman.domain.interactor.GetFavoritesUseCase
import ru.babaetskv.passionwoman.domain.interactor.GetProductUseCase
import ru.babaetskv.passionwoman.domain.interactor.exception.StringProvider
import ru.babaetskv.passionwoman.domain.model.Product
import ru.babaetskv.passionwoman.domain.model.Sorting
import ru.babaetskv.passionwoman.domain.preferences.FavoritesPreferences
import ru.babaetskv.passionwoman.domain.utils.execute
import kotlin.coroutines.CoroutineContext

class FavoritesViewModel(
    private val getFavoritesUseCase: GetFavoritesUseCase,
    private val getProductUseCase: GetProductUseCase,
    favoritesPreferences: FavoritesPreferences,
    val stringProvider: StringProvider,
    dependencies: ViewModelDependencies
) : BaseViewModel<FavoritesViewModel.Router>(dependencies) {
    private val favoritesActionsFlow: Flow<FavoritesPreferences.Action> =
        favoritesPreferences.favoritesUpdatesFlow
            .filterNotNull()
            .onEach(::onFavoritesUpdated)

    val productsLiveData = MutableLiveData<List<Product>>(emptyList())
    val sortingLiveData = MutableLiveData<Sorting>()

    init {
        favoritesActionsFlow.launchIn(this@FavoritesViewModel)
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

    sealed class Router : RouterEvent {

        data class ProductCardScreen(
            val product: Product
        ) : Router()
    }
}
