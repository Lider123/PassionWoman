package ru.babaetskv.passionwoman.app.presentation.feature.productcard

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.launch
import ru.babaetskv.passionwoman.app.R
import ru.babaetskv.passionwoman.app.analytics.event.AddToCartEvent
import ru.babaetskv.passionwoman.app.analytics.event.AddToWishlistEvent
import ru.babaetskv.passionwoman.app.presentation.base.BaseViewModel
import ru.babaetskv.passionwoman.app.presentation.base.ViewModelDependencies
import ru.babaetskv.passionwoman.app.presentation.event.InnerEvent
import ru.babaetskv.passionwoman.app.utils.deeplink.DeeplinkGenerator
import ru.babaetskv.passionwoman.app.utils.externalaction.ExternalActionHandler
import ru.babaetskv.passionwoman.domain.model.CartItem
import ru.babaetskv.passionwoman.domain.model.Product
import ru.babaetskv.passionwoman.domain.model.ProductColor
import ru.babaetskv.passionwoman.domain.model.ProductSize
import ru.babaetskv.passionwoman.domain.model.base.SelectableItem
import ru.babaetskv.passionwoman.domain.preferences.FavoritesPreferences
import ru.babaetskv.passionwoman.domain.usecase.AddToCartUseCase
import ru.babaetskv.passionwoman.domain.usecase.AddToFavoritesUseCase
import ru.babaetskv.passionwoman.domain.usecase.GetProductUseCase
import ru.babaetskv.passionwoman.domain.usecase.RemoveFromFavoritesUseCase

class ProductCardViewModelImpl(
    private val args: ProductCardFragment.Args,
    private val getProductUseCase: GetProductUseCase,
    private val favoritesPreferences: FavoritesPreferences,
    private val addToFavoritesUseCase: AddToFavoritesUseCase,
    private val removeFromFavoritesUseCase: RemoveFromFavoritesUseCase,
    private val addToCartUseCase: AddToCartUseCase,
    private val deeplinkGenerator: DeeplinkGenerator,
    private val externalActionHandler: ExternalActionHandler,
    dependencies: ViewModelDependencies
) : BaseViewModel<ProductCardViewModel.Router>(dependencies), ProductCardViewModel {
    override val productLiveData = MutableLiveData<Product>()
    override val productColorsLiveData = MutableLiveData<List<SelectableItem<ProductColor>>>()
    override val productPhotosLiveData = MutableLiveData<List<ProductImageItem>>()
    override val productSizesLiveData = MutableLiveData<List<SelectableItem<ProductSize>>>()
    override val isFavoriteLiveData = MutableLiveData<Boolean>()

    init {
        loadData()
    }

    override fun onErrorActionPressed() {
        loadData()
    }

    override fun onSizeItemPressed(item: SelectableItem<ProductSize>) {
        val size = item.value
        if (!size.isAvailable) return

        productSizesLiveData.value
            ?.map { it.copy(isSelected = size == it.value) }
            ?.let { productSizesLiveData.postValue(it) }
    }

    override fun onColorItemPressed(item: SelectableItem<ProductColor>) {
        productColorsLiveData.value
            ?.map { it.copy(isSelected = item.value == it.value) }
            ?.let { productColorsLiveData.postValue(it) }
        item.value.images
            .map(ProductImageItem::fromImage)
            .ifEmpty { listOf(ProductImageItem.EmptyPlaceholder) }
            .let { productPhotosLiveData.postValue(it) }
        val firstAvailableSize = item.value.sizes.firstOrNull { it.isAvailable }
        productSizesLiveData.postValue(item.value.sizes.map {
            SelectableItem(it, it == firstAvailableSize)
        })
    }

    override fun onFavoritePressed() {
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

    override fun onAddToCartPressed() {
        val selectedColor = productColorsLiveData.value
            ?.find { it.isSelected }
            ?.value
            ?.color
            ?: return

        val selectedSize = productSizesLiveData.value
            ?.find { it.isSelected }
            ?.value
            ?: return

        val product = productLiveData.value ?: return

        val cartItem = CartItem(
            product = product,
            selectedSize = selectedSize,
            selectedColor = selectedColor
        )
        launchWithLoading {
            addToCartUseCase.execute(cartItem)
            analyticsHandler.log(AddToCartEvent(product))
            notifier.newRequest(this, R.string.add_to_cart_success)
                .sendAlert()
            eventHub.post(InnerEvent.AddToCart(cartItem))
        }
    }

    override fun onSharePressed() {
        val product = productLiveData.value ?: return

        launch {
            val deeplink = deeplinkGenerator.createProductDeeplink(product) ?: return@launch

            externalActionHandler.handleText(deeplink)
        }
    }

    private fun loadData() {
        launchWithLoading {
            val product = getProductUseCase.execute(args.productId)
            productLiveData.postValue(product)
            productColorsLiveData.postValue(product.colors.mapIndexed { index, value ->
                SelectableItem(value, index == 0)
            })
            val firstColor = product.colors.first()
            firstColor.images
                .map(ProductImageItem::fromImage)
                .ifEmpty { listOf(ProductImageItem.EmptyPlaceholder) }
                .let { productPhotosLiveData.postValue(it) }
            val firstAvailableSize = firstColor.sizes.firstOrNull { it.isAvailable }
            productSizesLiveData.postValue(firstColor.sizes.map {
                SelectableItem(it, it == firstAvailableSize)
            })
            isFavoriteLiveData.postValue(favoritesPreferences.isFavorite(product.id))
        }
    }
}
