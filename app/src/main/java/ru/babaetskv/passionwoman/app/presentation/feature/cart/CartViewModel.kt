package ru.babaetskv.passionwoman.app.presentation.feature.cart

import androidx.lifecycle.LiveData
import ru.babaetskv.passionwoman.app.presentation.base.IViewModel
import ru.babaetskv.passionwoman.app.presentation.event.RouterEvent
import ru.babaetskv.passionwoman.domain.model.CartItem

interface CartViewModel : IViewModel {
    val cartItemsLiveData: LiveData<List<CartItem>>

    fun onAddCartItemPressed(item: CartItem)
    fun onRemoveCartItemPressed(item: CartItem)
    fun onCheckoutPressed()

    interface Router : RouterEvent
}
