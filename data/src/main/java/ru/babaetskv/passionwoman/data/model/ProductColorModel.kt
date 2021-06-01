package ru.babaetskv.passionwoman.data.model

import com.squareup.moshi.Json
import ru.babaetskv.passionwoman.domain.model.Image
import ru.babaetskv.passionwoman.domain.model.ProductColor

data class ProductColorModel(
    @Json(name = "color") val color: ColorModel,
    @Json(name = "images") val images: List<String>
) {

    fun toProductColor() =
        ProductColor(
            color = color.toColor(),
            images = images.map { Image(it) }
        )
}
