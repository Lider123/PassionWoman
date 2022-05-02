package ru.babaetskv.passionwoman.app.presentation.feature.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.babaetskv.passionwoman.app.R
import ru.babaetskv.passionwoman.app.presentation.base.BaseViewModel
import ru.babaetskv.passionwoman.app.presentation.base.ViewModelDependencies
import ru.babaetskv.passionwoman.domain.StringProvider
import ru.babaetskv.passionwoman.domain.cache.base.ListCache
import ru.babaetskv.passionwoman.domain.model.CartItem
import ru.babaetskv.passionwoman.domain.usecase.AddToCartUseCase
import ru.babaetskv.passionwoman.domain.usecase.GetCartItemsUseCase
import ru.babaetskv.passionwoman.domain.usecase.RemoveFromCartUseCase

class CartViewModelImpl(
    private val addToCartUseCase: AddToCartUseCase,
    private val removeFromCartUseCase: RemoveFromCartUseCase,
    private val cartItemsCache: ListCache<CartItem>,
    private val stringProvider: StringProvider,
    dependencies: ViewModelDependencies
) : BaseViewModel<CartViewModel.Router>(dependencies), CartViewModel {
    private val cartItemsFlow: Flow<List<CartItem>>
        get() = cartItemsCache.flow.onEach(::onCartItemsChanged)

    override val cartItemsLiveData: LiveData<List<CartItem>>
        get() = cartItemsFlow.asLiveData(coroutineContext)

    init {
        cartItemsFlow.launchIn(this)
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
        // TODO: remove
        notifier.newRequest(this, R.string.in_development)
            .sendAlert()
    }

    private fun onCartItemsChanged(items: List<CartItem>) {
        if (items.isEmpty()) {
            onError(coroutineContext, GetCartItemsUseCase.EmptyCartItemsException(stringProvider))
        }
    }
}
