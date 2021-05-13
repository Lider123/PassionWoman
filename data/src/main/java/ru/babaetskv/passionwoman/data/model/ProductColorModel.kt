package ru.babaetskv.passionwoman.data.model

import ru.babaetskv.passionwoman.domain.model.ProductColor

data class ProductColorModel(
    val name: String,
    val hex: String
) {

    fun toProductColor() =
        ProductColor(
            name = name,
            hex = hex
        )
}
