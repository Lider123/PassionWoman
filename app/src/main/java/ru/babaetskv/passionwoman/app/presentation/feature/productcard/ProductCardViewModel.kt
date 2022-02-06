package ru.babaetskv.passionwoman.app.presentation.feature.productcard

import androidx.lifecycle.LiveData
import ru.babaetskv.passionwoman.app.presentation.base.IViewModel
import ru.babaetskv.passionwoman.app.presentation.event.RouterEvent
import ru.babaetskv.passionwoman.domain.model.Image
import ru.babaetskv.passionwoman.domain.model.Product
import ru.babaetskv.passionwoman.domain.model.ProductColor
import ru.babaetskv.passionwoman.domain.model.ProductSize
import ru.babaetskv.passionwoman.domain.model.base.SelectableItem

interface ProductCardViewModel : IViewModel {
    val productLiveData: LiveData<Product>
    val productColorsLiveData: LiveData<List<SelectableItem<ProductColor>>>
    val productPhotosLiveData: LiveData<List<Image>>
    val productSizesLiveData: LiveData<List<SelectableItem<ProductSize>>>
    val isFavoriteLiveData: LiveData<Boolean>

    fun onSizeItemPressed(item: SelectableItem<ProductSize>)
    fun onColorItemPressed(item: SelectableItem<ProductColor>)
    fun onFavoritePressed()
    fun onAddToCartPressed()
    fun onSharePressed()

    sealed class Router : RouterEvent
}
