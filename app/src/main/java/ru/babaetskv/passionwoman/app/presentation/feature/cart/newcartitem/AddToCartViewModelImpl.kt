package ru.babaetskv.passionwoman.app.presentation.feature.cart.newcartitem

import androidx.lifecycle.MutableLiveData
import ru.babaetskv.passionwoman.app.presentation.base.BaseViewModel
import ru.babaetskv.passionwoman.app.presentation.base.ViewModelDependencies
import ru.babaetskv.passionwoman.domain.model.*
import ru.babaetskv.passionwoman.domain.model.base.SelectableItem

class AddToCartViewModelImpl(
    private val args: AddToCartFragment.Args,
    dependencies: ViewModelDependencies
) : BaseViewModel<AddToCartViewModel.Router>(dependencies), AddToCartViewModel {
    private var selectedProductColor: ProductColor = args.product.colors[0]
    private var selectedProductSize: ProductSize? = selectedProductColor.sizes.getOrNull(0)
    private val product: Product
        get() = args.product

    override val addToCartItemsLiveData = MutableLiveData<List<AddToCartItem>>(emptyList())

    init {
        updateItems()
    }

    override fun onColorPressed(color: SelectableItem<Color>) {
        selectedProductColor = product.colors.find { it.color.code == color.value.code } ?: return

        selectedProductSize = selectedProductColor.sizes.getOrNull(0) ?: ProductSize.EMPTY
        updateItems()
    }

    override fun onSizePressed(size: SelectableItem<ProductSize>) {
        selectedProductSize = size.value
        updateItems()
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
        if (selectedProductColor.sizes.isNotEmpty()) {
            val sizes = selectedProductColor.sizes.map {
                SelectableItem(it,
                    isSelected = it.value == selectedProductSize?.value
                )
            }
            items.add(AddToCartItem.Sizes(sizes))
        }
        addToCartItemsLiveData.postValue(items)
    }
}
