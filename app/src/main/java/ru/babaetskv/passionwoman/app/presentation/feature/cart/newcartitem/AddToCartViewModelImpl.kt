package ru.babaetskv.passionwoman.app.presentation.feature.cart.newcartitem

import androidx.lifecycle.MutableLiveData
import ru.babaetskv.passionwoman.app.R
import ru.babaetskv.passionwoman.app.analytics.event.AddToCartEvent
import ru.babaetskv.passionwoman.app.presentation.base.BaseViewModel
import ru.babaetskv.passionwoman.app.presentation.base.ViewModelDependencies
import ru.babaetskv.passionwoman.app.presentation.event.InnerEvent
import ru.babaetskv.passionwoman.domain.model.*
import ru.babaetskv.passionwoman.domain.model.base.SelectableItem
import ru.babaetskv.passionwoman.domain.usecase.AddToCartUseCase

class AddToCartViewModelImpl(
    private val args: AddToCartFragment.Args,
    private val addToCartUseCase: AddToCartUseCase,
    dependencies: ViewModelDependencies
) : BaseViewModel<AddToCartViewModel.Router>(dependencies), AddToCartViewModel {
    private var selectedProductColor: ProductColor = args.product.colors[0]
    private var selectedProductSize: ProductSize? = availableProductSizes.getOrNull(0)
    private val product: Product
        get() = args.product
    private val availableProductSizes: List<ProductSize>
        get() = selectedProductColor.sizes.filter(ProductSize::isAvailable)

    override val addToCartItemsLiveData = MutableLiveData<List<AddToCartItem>>(emptyList())

    init {
        updateItems()
    }

    override fun onColorPressed(color: SelectableItem<Color>) {
        selectedProductColor = product.colors.find { it.color.code == color.value.code } ?: return

        selectedProductSize = availableProductSizes.getOrNull(0) ?: ProductSize.EMPTY
        updateItems()
    }

    override fun onSizePressed(size: SelectableItem<ProductSize>) {
        selectedProductSize = size.value
        updateItems()
    }

    override fun onConfirmPressed() {
        val selectedSize = selectedProductSize ?: return

        val cartItem = CartItem(
            product = product,
            selectedSize = selectedSize,
            selectedColor = selectedProductColor.color
        )
        launchWithLoading {
            addToCartUseCase.execute(cartItem)
            analyticsHandler.log(AddToCartEvent(product))
            notifier.newRequest(this, R.string.add_to_cart_success)
                .sendAlert()
            eventHub.post(InnerEvent.AddToCart(cartItem))
            onBackPressed()
        }
    }

    private fun updateItems() {
        val newCartItem = CartItem(
            product = product,
            selectedColor = selectedProductColor.color,
            selectedSize = selectedProductSize ?: ProductSize.EMPTY
        )
        val colors = product.colors.map {
            SelectableItem(it.color,
                isSelected = it.color.code == selectedProductColor.color.code
            )
        }
        val items = mutableListOf(
            AddToCartItem.ProductDescription(newCartItem),
            AddToCartItem.Colors(colors)
        )
        if (selectedProductColor.sizes.any(ProductSize::isAvailable)) {
            val sizes = availableProductSizes.map {
                SelectableItem(it,
                    isSelected = it.value == selectedProductSize?.value
                )
            }
            items.add(AddToCartItem.Sizes(sizes))
        }
        val productIsAvailable = selectedProductColor.sizes.any { it.isAvailable }
        items.add(AddToCartItem.Confirmation(productIsAvailable))
        addToCartItemsLiveData.postValue(items)
    }
}
