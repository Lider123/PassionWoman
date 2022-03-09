package ru.babaetskv.passionwoman.app.presentation.feature.productcard

import ru.babaetskv.passionwoman.domain.model.Image

sealed class ProductImageItem {

    data class ProductImage(
        val data: Image
    ) : ProductImageItem()

    object EmptyPlaceholder : ProductImageItem()

    companion object {

        fun fromImage(image: Image) = ProductImage(image)
    }
}
