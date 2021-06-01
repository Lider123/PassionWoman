package ru.babaetskv.passionwoman.app.presentation.feature.productcard

import ru.babaetskv.passionwoman.domain.model.ProductColor

data class ProductColorItem(
    val productColor: ProductColor,
    val selected: Boolean = false
) {

    companion object {

        fun fromProductColor(productColor: ProductColor) = ProductColorItem(productColor)
    }
}
