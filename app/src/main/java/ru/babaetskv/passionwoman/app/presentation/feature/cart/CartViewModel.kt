package ru.babaetskv.passionwoman.app.presentation.feature.cart

import androidx.lifecycle.LiveData
import ru.babaetskv.passionwoman.app.presentation.base.IViewModel
import ru.babaetskv.passionwoman.app.presentation.event.RouterEvent
import ru.babaetskv.passionwoman.domain.model.Cart
import ru.babaetskv.passionwoman.domain.model.CartItem

interface CartViewModel : IViewModel {
    val cartLiveData: LiveData<Cart>

    fun onAddCartItemPressed(item: CartItem)
    fun onRemoveCartItemPressed(item: CartItem)
    fun onCheckoutPressed()

    sealed class Router : RouterEvent {

        object Orders : Router()
    }
}
