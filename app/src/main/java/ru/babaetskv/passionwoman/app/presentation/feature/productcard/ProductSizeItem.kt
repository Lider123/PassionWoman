package ru.babaetskv.passionwoman.app.presentation.feature.productcard

import ru.babaetskv.passionwoman.domain.model.ProductSize

@Deprecated("Use SelectableItem instead")
data class ProductSizeItem(
    val size: ProductSize,
    val isSelected: Boolean = false
)
