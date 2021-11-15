package ru.babaetskv.passionwoman.data.model

import com.squareup.moshi.Json
import ru.babaetskv.passionwoman.domain.model.Image
import ru.babaetskv.passionwoman.domain.model.ProductColor
import ru.babaetskv.passionwoman.domain.model.ProductSize

data class ProductColorModel(
    @Json(name = "color") val color: ColorModel,
    @Json(name = "sizes") val sizes: List<String>?,
    @Json(name = "availableSizes") val availableSizes: List<String>?,
    @Json(name = "images") val images: List<String>
) {

    fun toProductColor() =
        ProductColor(
            color = color.toColor(),
            sizes = sizes?.map {
                ProductSize(it,
                    isAvailable = availableSizes?.contains(it) ?: false
                )
            } ?: emptyList(),
            images = images.map { Image(it) }
        )
}
