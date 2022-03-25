package ru.babaetskv.passionwoman.app.presentation.feature.cart.newcartitem

import androidx.lifecycle.LiveData
import ru.babaetskv.passionwoman.app.presentation.base.IViewModel
import ru.babaetskv.passionwoman.app.presentation.event.RouterEvent

interface AddToCartViewModel : IViewModel {
    val addToCartItemsLiveData: LiveData<List<AddToCartItem>>

    interface Router : RouterEvent
}
