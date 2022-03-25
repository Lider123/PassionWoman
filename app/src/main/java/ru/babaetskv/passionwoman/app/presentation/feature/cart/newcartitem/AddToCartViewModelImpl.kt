package ru.babaetskv.passionwoman.app.presentation.feature.cart.newcartitem

import androidx.lifecycle.MutableLiveData
import ru.babaetskv.passionwoman.app.presentation.base.BaseViewModel
import ru.babaetskv.passionwoman.app.presentation.base.ViewModelDependencies
import ru.babaetskv.passionwoman.domain.model.CartItem
import ru.babaetskv.passionwoman.domain.model.ProductSize

class AddToCartViewModelImpl(
    args: AddToCartFragment.Args,
    dependencies: ViewModelDependencies
) : BaseViewModel<AddToCartViewModel.Router>(dependencies), AddToCartViewModel {
    private val emptySize = ProductSize("", false)

    override val addToCartItemsLiveData = MutableLiveData<List<AddToCartItem>>(emptyList())

    init {
        // TODO
        val newCartItem = CartItem(
            product = args.product,
            selectedColor = args.product.colors[0].color,
            selectedSize = args.product.colors[0].sizes.getOrNull(0) ?: ProductSize.EMPTY
        )
        val items = listOf(
            AddToCartItem.ProductDescription(newCartItem)
        )
        addToCartItemsLiveData.postValue(items)
    }
}
