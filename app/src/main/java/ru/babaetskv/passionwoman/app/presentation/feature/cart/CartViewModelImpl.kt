package ru.babaetskv.passionwoman.app.presentation.feature.cart

import androidx.lifecycle.MutableLiveData
import ru.babaetskv.passionwoman.app.R
import ru.babaetskv.passionwoman.app.presentation.base.BaseViewModel
import ru.babaetskv.passionwoman.app.presentation.base.ViewModelDependencies
import ru.babaetskv.passionwoman.app.presentation.event.InnerEvent
import ru.babaetskv.passionwoman.domain.model.*
import ru.babaetskv.passionwoman.domain.usecase.GetCartItemsUseCase

class CartViewModelImpl(
    private val getCartItemsUseCase: GetCartItemsUseCase,
    dependencies: ViewModelDependencies
) : BaseViewModel<CartViewModel.Router>(dependencies), CartViewModel {
    override val cartItemsLiveData = MutableLiveData<List<CartItem>>()

    init {
        loadCartItems()
    }

    override fun onEvent(event: InnerEvent) {
        when (event) {
            is InnerEvent.AddToCart -> loadCartItems()
            else -> super.onEvent(event)
        }
    }

    override fun onAddCartItemPressed(item: CartItem) {
        // TODO: remove
        notifier.newRequest(this, R.string.in_development)
            .sendAlert()
    }

    override fun onRemoveCartItemPressed(item: CartItem) {
        // TODO: remove
        notifier.newRequest(this, R.string.in_development)
            .sendAlert()
    }

    override fun onCheckoutPressed() {
        // TODO: remove
        notifier.newRequest(this, R.string.in_development)
            .sendAlert()
    }

    private fun loadCartItems() {
        launchWithLoading {
            cartItemsLiveData.postValue(getCartItemsUseCase.execute())
        }
    }
}
