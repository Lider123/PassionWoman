package ru.babaetskv.passionwoman.app.presentation.feature.cart.newcartitem

import ru.babaetskv.passionwoman.domain.model.CartItem
import ru.babaetskv.passionwoman.domain.model.Color
import ru.babaetskv.passionwoman.domain.model.ProductSize
import ru.babaetskv.passionwoman.domain.model.base.SelectableItem

sealed class AddToCartItem {

    data class ProductDescription(
        val item: CartItem
    ) : AddToCartItem()

    data class Sizes(
        val sizes: List<SelectableItem<ProductSize>>
    ) : AddToCartItem()

    data class Colors(
        val colors: List<SelectableItem<Color>>
    ) : AddToCartItem()

    // TODO: add confirm button
}
