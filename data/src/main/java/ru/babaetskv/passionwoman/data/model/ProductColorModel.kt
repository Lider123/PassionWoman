package ru.babaetskv.passionwoman.data.model

import com.squareup.moshi.Json
import ru.babaetskv.passionwoman.domain.model.Image
import ru.babaetskv.passionwoman.domain.model.ProductColor
import ru.babaetskv.passionwoman.domain.model.ProductSize
import ru.babaetskv.passionwoman.domain.model.base.Transformable
import ru.babaetskv.passionwoman.domain.utils.transform

data class ProductColorModel(
    @Json(name = "color") val color: ColorModel,
    @Json(name = "sizes") val sizes: List<String>?,
    @Json(name = "availableSizes") val availableSizes: List<String>?,
    @Json(name = "images") val images: List<String>
) : Transformable<Unit, ProductColor> {

    override fun transform(params: Unit): ProductColor =
        ProductColor(
            color = color.transform(),
            sizes = sizes?.map {
                ProductSize(it,
                    isAvailable = availableSizes?.contains(it) ?: false
                )
            } ?: emptyList(),
            images = images.map(::Image)
        )
}
