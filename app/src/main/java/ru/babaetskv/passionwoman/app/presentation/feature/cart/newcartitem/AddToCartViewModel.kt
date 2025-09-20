package ru.babaetskv.passionwoman.app.presentation.feature.cart.newcartitem

import androidx.lifecycle.LiveData
import ru.babaetskv.passionwoman.app.presentation.base.IViewModel
import ru.babaetskv.passionwoman.domain.model.Color
import ru.babaetskv.passionwoman.domain.model.ProductSize
import ru.babaetskv.passionwoman.domain.model.base.SelectableItem

interface AddToCartViewModel : IViewModel {
    val addToCartItemsLiveData: LiveData<List<AddToCartItem>>

    fun onColorPressed(color: SelectableItem<Color>)
    fun onSizePressed(size: SelectableItem<ProductSize>)
    fun onConfirmPressed()
}
