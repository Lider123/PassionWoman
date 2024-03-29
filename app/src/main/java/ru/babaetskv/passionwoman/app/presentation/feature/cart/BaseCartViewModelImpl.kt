package ru.babaetskv.passionwoman.app.presentation.feature.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.babaetskv.passionwoman.app.R
import ru.babaetskv.passionwoman.app.navigation.ScreenProvider
import ru.babaetskv.passionwoman.app.presentation.base.BaseViewModel
import ru.babaetskv.passionwoman.app.presentation.base.ViewModelDependencies
import ru.babaetskv.passionwoman.domain.StringProvider
import ru.babaetskv.passionwoman.domain.dataflow.CartFlow
import ru.babaetskv.passionwoman.domain.model.Cart
import ru.babaetskv.passionwoman.domain.model.CartItem
import ru.babaetskv.passionwoman.domain.usecase.AddToCartUseCase
import ru.babaetskv.passionwoman.domain.usecase.CheckoutUseCase
import ru.babaetskv.passionwoman.domain.usecase.RemoveFromCartUseCase

// TODO: if profile is not filled when checkout pressed then ask to fill it
abstract class BaseCartViewModelImpl(
    private val addToCartUseCase: AddToCartUseCase,
    private val removeFromCartUseCase: RemoveFromCartUseCase,
    private val cartFlow: CartFlow, // TODO: replace with use case subscription
    private val checkoutUseCase: CheckoutUseCase,
    private val stringProvider: StringProvider,
    dependencies: ViewModelDependencies
) : BaseViewModel(dependencies), CartViewModel {
    private val mCartFlow: Flow<Cart>
        get() = cartFlow.subscribe().onEach {
            val error = if (it.isEmpty) {
                // TODO: think up how to throw this error outside ViewModel
                CartFlow.EmptyCartException(stringProvider)
            } else null
            errorLiveData.postValue(error)
        }

    override val cartLiveData: LiveData<Cart>
        get() = mCartFlow.asLiveData(coroutineContext)

    init {
        mCartFlow.launchIn(this)
    }

    override fun onAddCartItemPressed(item: CartItem) {
        launchWithLoading {
            val newItem = item.copy(count = 1)
            addToCartUseCase.execute(newItem)
        }
    }

    override fun onRemoveCartItemPressed(item: CartItem) {
        launchWithLoading {
            val itemToRemove = item.copy(count = 1)
            removeFromCartUseCase.execute(itemToRemove)
        }
    }

    override fun onCheckoutPressed() {
        // TODO: handle payment
        launchWithLoading {
            checkout()
        }
    }

    protected open suspend fun checkout(): Long {
        val orderId = checkoutUseCase.execute(Unit)
        notifier.newRequest(this, R.string.cart_order_created)
            .sendAlert()
        router.navigateTo(ScreenProvider.orders())
        return orderId
    }
}
