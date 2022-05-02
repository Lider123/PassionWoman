package ru.babaetskv.passionwoman.app.presentation.feature.cart

import androidx.lifecycle.LiveData
import ru.babaetskv.passionwoman.app.R
import ru.babaetskv.passionwoman.app.presentation.base.BaseViewModel
import ru.babaetskv.passionwoman.app.presentation.base.ViewModelDependencies
import ru.babaetskv.passionwoman.app.presentation.event.InnerEvent
import ru.babaetskv.passionwoman.domain.cache.base.ListCache
import ru.babaetskv.passionwoman.domain.model.*
import ru.babaetskv.passionwoman.domain.usecase.AddToCartUseCase
import ru.babaetskv.passionwoman.domain.usecase.RemoveFromCartUseCase

class CartViewModelImpl(
    private val addToCartUseCase: AddToCartUseCase,
    private val removeFromCartUseCase: RemoveFromCartUseCase,
    private val cartItemsCache: ListCache<CartItem>,
    dependencies: ViewModelDependencies
) : BaseViewModel<CartViewModel.Router>(dependencies), CartViewModel {
    override val cartItemsLiveData: LiveData<List<CartItem>>
        get() = cartItemsCache.liveData

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
}
