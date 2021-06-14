package ru.babaetskv.passionwoman.app.presentation.feature.productcard

import androidx.lifecycle.MutableLiveData
import ru.babaetskv.passionwoman.app.R
import ru.babaetskv.passionwoman.app.navigation.AppRouter
import ru.babaetskv.passionwoman.app.presentation.base.BaseViewModel
import ru.babaetskv.passionwoman.app.utils.notifier.Notifier

class ProductCardViewModel(
    args: ProductCardFragment.Args,
    notifier: Notifier,
    router: AppRouter
) : BaseViewModel(notifier, router) {
    val productLiveData = MutableLiveData(args.product)
    val productColorsLiveData = MutableLiveData(args.product.colors.mapIndexed { index, value ->
        ProductColorItem(value, index == 0)
    })
    val productPhotosLiveData = MutableLiveData(args.product.colors.first().images)

    fun onColorItemPressed(item: ProductColorItem) {
        val newItems = productColorsLiveData.value?.map {
            it.copy(selected = item.productColor == it.productColor)
        }
        productColorsLiveData.postValue(newItems)
        productPhotosLiveData.postValue(item.productColor.images)
    }

    fun onFavoritePressed() {
        // TODO
        notifier.newRequest(this, R.string.in_development)
            .sendAlert()
    }

    fun onAddToCartPressed() {
        // TODO
        notifier.newRequest(this, R.string.in_development)
            .sendAlert()
    }
}
