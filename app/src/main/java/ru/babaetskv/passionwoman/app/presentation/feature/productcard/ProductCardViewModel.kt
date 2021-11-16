package ru.babaetskv.passionwoman.app.presentation.feature.productcard

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.launch
import ru.babaetskv.passionwoman.app.R
import ru.babaetskv.passionwoman.app.analytics.event.AddToWishlistEvent
import ru.babaetskv.passionwoman.app.presentation.base.BaseViewModel
import ru.babaetskv.passionwoman.app.presentation.base.RouterEvent
import ru.babaetskv.passionwoman.app.presentation.base.ViewModelDependencies
import ru.babaetskv.passionwoman.app.utils.ExternalIntentHandler
import ru.babaetskv.passionwoman.app.utils.deeplink.DeeplinkGenerator
import ru.babaetskv.passionwoman.domain.interactor.AddToFavoritesUseCase
import ru.babaetskv.passionwoman.domain.interactor.GetProductUseCase
import ru.babaetskv.passionwoman.domain.interactor.RemoveFromFavoritesUseCase
import ru.babaetskv.passionwoman.domain.model.Image
import ru.babaetskv.passionwoman.domain.model.Product
import ru.babaetskv.passionwoman.domain.model.ProductColor
import ru.babaetskv.passionwoman.domain.model.ProductSize
import ru.babaetskv.passionwoman.domain.model.base.SelectableItem
import ru.babaetskv.passionwoman.domain.preferences.FavoritesPreferences

class ProductCardViewModel(
    private val args: ProductCardFragment.Args,
    private val getProductUseCase: GetProductUseCase,
    private val favoritesPreferences: FavoritesPreferences,
    private val addToFavoritesUseCase: AddToFavoritesUseCase,
    private val removeFromFavoritesUseCase: RemoveFromFavoritesUseCase,
    private val deeplinkGenerator: DeeplinkGenerator,
    private val externalIntentHandler: ExternalIntentHandler,
    dependencies: ViewModelDependencies
) : BaseViewModel<ProductCardViewModel.Router>(dependencies) {
    val productLiveData = MutableLiveData<Product>()
    val productColorsLiveData = MutableLiveData<List<SelectableItem<ProductColor>>>()
    val productPhotosLiveData = MutableLiveData<List<Image>>()
    val productSizesLiveData = MutableLiveData<List<SelectableItem<ProductSize>>>()
    val isFavoriteLiveData = MutableLiveData<Boolean>()

    init {
        loadData()
    }

    override fun onErrorActionPressed() {
        loadData()
    }

    private fun loadData() {
        launchWithLoading {
            val product = getProductUseCase.execute(args.productId)
            productLiveData.postValue(product)
            productColorsLiveData.postValue(product.colors.mapIndexed { index, value ->
                SelectableItem(value, index == 0)
            })
            val firstColor = product.colors.first()
            productPhotosLiveData.postValue(firstColor.images)
            val firstAvailableSize = firstColor.sizes.firstOrNull { it.isAvailable }
            productSizesLiveData.postValue(firstColor.sizes.map {
                SelectableItem(it, it == firstAvailableSize)
            })
            isFavoriteLiveData.postValue(favoritesPreferences.isFavorite(product.id))
        }
    }

    fun onSizeItemPressed(item: SelectableItem<ProductSize>) {
        val size = item.value
        if (!size.isAvailable) return

        val newItems = productSizesLiveData.value?.map {
            it.copy(isSelected = size == it.value)
        }
        productSizesLiveData.postValue(newItems)
    }

    fun onColorItemPressed(item: SelectableItem<ProductColor>) {
        val newItems = productColorsLiveData.value?.map {
            it.copy(isSelected = item.value == it.value)
        }
        productColorsLiveData.postValue(newItems)
        productPhotosLiveData.postValue(item.value.images)
        val firstAvailableSize = item.value.sizes.firstOrNull { it.isAvailable }
        productSizesLiveData.postValue(item.value.sizes.map {
            SelectableItem(it, it == firstAvailableSize)
        })
    }

    fun onFavoritePressed() {
        val product = productLiveData.value ?: return

        val isFavorite = isFavoriteLiveData.value ?: return

        launchWithLoading {
            val productId = product.id
            if (isFavorite) {
                removeFromFavoritesUseCase.execute(productId)
            } else {
                addToFavoritesUseCase.execute(productId)
                analyticsHandler.log(AddToWishlistEvent(product))
            }
            isFavoriteLiveData.postValue(favoritesPreferences.isFavorite(productId))
        }
    }

    fun onAddToCartPressed() {
        // TODO
        notifier.newRequest(this, R.string.in_development)
            .sendAlert()
    }

    fun onSharePressed() {
        val product = productLiveData.value ?: return

        launch {
            val deeplink = deeplinkGenerator.createProductDeeplink(product) ?: return@launch
            
            externalIntentHandler.handleText(deeplink)
        }
    }

    sealed class Router : RouterEvent
}
