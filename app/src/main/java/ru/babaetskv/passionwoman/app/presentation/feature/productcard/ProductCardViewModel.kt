package ru.babaetskv.passionwoman.app.presentation.feature.productcard

import androidx.lifecycle.MutableLiveData
import ru.babaetskv.passionwoman.app.R
import ru.babaetskv.passionwoman.app.presentation.base.BaseViewModel
import ru.babaetskv.passionwoman.app.presentation.base.RouterEvent
import ru.babaetskv.passionwoman.app.utils.notifier.Notifier
import ru.babaetskv.passionwoman.domain.interactor.AddToFavoritesUseCase
import ru.babaetskv.passionwoman.domain.interactor.GetProductUseCase
import ru.babaetskv.passionwoman.domain.interactor.RemoveFromFavoritesUseCase
import ru.babaetskv.passionwoman.domain.model.Image
import ru.babaetskv.passionwoman.domain.model.Product
import ru.babaetskv.passionwoman.domain.preferences.FavoritesPreferences

class ProductCardViewModel(
    private val args: ProductCardFragment.Args,
    private val getProductUseCase: GetProductUseCase,
    private val favoritesPreferences: FavoritesPreferences,
    private val addToFavoritesUseCase: AddToFavoritesUseCase,
    private val removeFromFavoritesUseCase: RemoveFromFavoritesUseCase,
    notifier: Notifier
) : BaseViewModel<ProductCardViewModel.Router>(notifier) {
    val productLiveData = MutableLiveData<Product>()
    val productColorsLiveData = MutableLiveData<List<ProductColorItem>>()
    val productPhotosLiveData = MutableLiveData<List<Image>>()
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
                ProductColorItem(value, index == 0)
            })
            productPhotosLiveData.postValue(product.colors.first().images)
            isFavoriteLiveData.postValue(favoritesPreferences.isFavorite(product.id))
        }
    }

    fun onColorItemPressed(item: ProductColorItem) {
        val newItems = productColorsLiveData.value?.map {
            it.copy(selected = item.productColor == it.productColor)
        }
        productColorsLiveData.postValue(newItems)
        productPhotosLiveData.postValue(item.productColor.images)
    }

    fun onFavoritePressed() {
        val productId = productLiveData.value?.id ?: return

        val isFavorite = isFavoriteLiveData.value ?: return

        launchWithLoading {
            if (isFavorite) {
                removeFromFavoritesUseCase.execute(productId)
            } else {
                addToFavoritesUseCase.execute(productId)
            }
            isFavoriteLiveData.postValue(favoritesPreferences.isFavorite(productId))
        }
    }

    fun onAddToCartPressed() {
        // TODO
        notifier.newRequest(this, R.string.in_development)
            .sendAlert()
    }

    sealed class Router : RouterEvent
}
