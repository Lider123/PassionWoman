package ru.babaetskv.passionwoman.app.presentation.feature.productlist

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.babaetskv.passionwoman.app.analytics.event.SelectProductEvent
import ru.babaetskv.passionwoman.app.navigation.ScreenProvider
import ru.babaetskv.passionwoman.app.presentation.base.BaseViewModel
import ru.babaetskv.passionwoman.app.presentation.base.ViewModelDependencies
import ru.babaetskv.passionwoman.domain.StringProvider
import ru.babaetskv.passionwoman.domain.model.Product
import ru.babaetskv.passionwoman.domain.model.Sorting
import ru.babaetskv.passionwoman.domain.preferences.FavoritesPreferences
import ru.babaetskv.passionwoman.domain.usecase.GetFavoritesUseCase
import ru.babaetskv.passionwoman.domain.usecase.GetProductUseCase
import ru.babaetskv.passionwoman.domain.usecase.base.UseCase.Companion.execute
import java.lang.Exception
import kotlin.coroutines.CoroutineContext

class FavoritesViewModelImpl(
    private val getFavoritesUseCase: GetFavoritesUseCase,
    private val getProductUseCase: GetProductUseCase,
    favoritesPreferences: FavoritesPreferences,
    override val stringProvider: StringProvider,
    dependencies: ViewModelDependencies
) : BaseViewModel(dependencies), FavoritesViewModel {
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

    override fun onErrorActionPressed(exception: Exception) {
        when(exception) {
            is GetFavoritesUseCase.GetFavoritesException -> loadFavorites()
            else -> super.onErrorActionPressed(exception)
        }
    }

    override fun onProductPressed(product: Product) {
        analyticsHandler.log(SelectProductEvent(product))

        if (isPortraitModeOnly) {
            router.navigateTo(ScreenProvider.productCard(product.id))
        } else {
            launch {
                sendEvent(FavoritesViewModel.OpenLandscapeProductCardEvent(product))
            }
        }
    }

    override fun onBuyPressed(product: Product) {
        router.openBottomSheet(ScreenProvider.newCartItem(product))
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
