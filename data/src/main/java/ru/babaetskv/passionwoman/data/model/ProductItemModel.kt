package ru.babaetskv.passionwoman.data.model

import ru.babaetskv.passionwoman.domain.model.Image
import ru.babaetskv.passionwoman.domain.model.ProductItem

data class ProductItemModel(
    val color: ProductColorModel,
    val images: List<String>,
    val price: Float,
    val rating: Float
) {

    fun toProductItem() =
        ProductItem(
            color = color.toProductColor(),
            images = images.map { Image(it) },
            price = price,
            rating = rating
        )
}
