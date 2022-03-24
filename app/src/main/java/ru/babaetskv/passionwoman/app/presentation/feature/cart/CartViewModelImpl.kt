package ru.babaetskv.passionwoman.app.presentation.feature.cart

import androidx.lifecycle.MutableLiveData
import ru.babaetskv.passionwoman.app.R
import ru.babaetskv.passionwoman.app.presentation.base.BaseViewModel
import ru.babaetskv.passionwoman.app.presentation.base.ViewModelDependencies
import ru.babaetskv.passionwoman.app.presentation.event.InnerEvent
import ru.babaetskv.passionwoman.domain.model.*
import ru.babaetskv.passionwoman.domain.usecase.AddToCartUseCase
import ru.babaetskv.passionwoman.domain.usecase.GetCartItemsUseCase
import ru.babaetskv.passionwoman.domain.usecase.RemoveFromCartUseCase

class CartViewModelImpl(
    private val getCartItemsUseCase: GetCartItemsUseCase,
    private val addToCartUseCase: AddToCartUseCase,
    private val removeFromCartUseCase: RemoveFromCartUseCase,
    dependencies: ViewModelDependencies
) : BaseViewModel<CartViewModel.Router>(dependencies), CartViewModel {
    override val cartItemsLiveData = MutableLiveData<List<CartItem>>()

    init {
        loadCartItems()
    }

    override fun onEvent(event: InnerEvent) {
        when (event) {
            is InnerEvent.AddToCart, is InnerEvent.RemoveFromCart -> loadCartItems()
            else -> super.onEvent(event)
        }
    }

    override fun onAddCartItemPressed(item: CartItem) {
        launchWithLoading {
            val newItem = item.copy(count = 1)
            addToCartUseCase.execute(newItem)
            eventHub.post(InnerEvent.AddToCart(newItem))
        }
    }

    override fun onRemoveCartItemPressed(item: CartItem) {
        launchWithLoading {
            val itemToRemove = item.copy(count = 1)
            removeFromCartUseCase.execute(itemToRemove)
            eventHub.post(InnerEvent.RemoveFromCart(itemToRemove))
        }
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
