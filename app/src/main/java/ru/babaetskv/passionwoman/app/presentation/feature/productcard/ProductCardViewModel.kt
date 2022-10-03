package ru.babaetskv.passionwoman.app.presentation.feature.productcard

import androidx.lifecycle.LiveData
import ru.babaetskv.passionwoman.app.presentation.base.IViewModel
import ru.babaetskv.passionwoman.domain.model.Color
import ru.babaetskv.passionwoman.domain.model.Product
import ru.babaetskv.passionwoman.domain.model.ProductSize
import ru.babaetskv.passionwoman.domain.model.base.SelectableItem

interface ProductCardViewModel : IViewModel {
    val productLiveData: LiveData<Product>
    val colorsLiveData: LiveData<List<SelectableItem<Color>>>
    val productPhotosLiveData: LiveData<List<ProductImageItem>>
    val productSizesLiveData: LiveData<List<SelectableItem<ProductSize>>>
    val isFavoriteLiveData: LiveData<Boolean>

    fun onSizeItemPressed(item: SelectableItem<ProductSize>)
    fun onColorItemPressed(item: SelectableItem<Color>)
    fun onFavoritePressed()
    fun onAddToCartPressed()
    fun onSharePressed()
}
