package ru.babaetskv.passionwoman.app.presentation.feature.cart.newcartitem

import ru.babaetskv.passionwoman.domain.model.CartItem
import ru.babaetskv.passionwoman.domain.model.Color
import ru.babaetskv.passionwoman.domain.model.ProductSize

sealed class AddToCartItem {

    data class ProductDescription(
        val item: CartItem
    ) : AddToCartItem()

    data class Sizes(
        val sizes: List<ProductSize>
    ) : AddToCartItem()

    data class Colors(
        val colors: List<Color>
    ) : AddToCartItem()
}
