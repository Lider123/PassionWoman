package ru.babaetskv.passionwoman.app.presentation.feature.productcard

import ru.babaetskv.passionwoman.domain.model.ProductSize

data class ProductSizeItem(
    val size: ProductSize,
    val isSelected: Boolean
) {

    companion object {

        fun fromProductSize(size: ProductSize) = ProductSizeItem(size, false)
    }
}
