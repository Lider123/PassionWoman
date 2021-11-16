package ru.babaetskv.passionwoman.app.presentation.feature.productcard

import ru.babaetskv.passionwoman.domain.model.ProductColor

@Deprecated("Use SelectableItem instead")
data class ProductColorItem(
    val productColor: ProductColor,
    val selected: Boolean = false
)
