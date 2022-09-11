package ru.babaetskv.passionwoman.data.model

import com.squareup.moshi.Json
import ru.babaetskv.passionwoman.domain.model.Image
import ru.babaetskv.passionwoman.domain.model.ProductItem
import ru.babaetskv.passionwoman.domain.model.ProductSize
import ru.babaetskv.passionwoman.domain.model.base.Transformable

data class ProductItemModel(
    @Json(name = "color") val color: ColorModel,
    @Json(name = "sizes") val sizes: List<String>?,
    @Json(name = "availableSizes") val availableSizes: List<String>?,
    @Json(name = "images") val images: List<String>
) : Transformable<Unit, ProductItem>() {

    override suspend fun transform(params: Unit): ProductItem =
        ProductItem(
            color = color.transform(),
            sizes = sizes?.map {
                ProductSize(it,
                    isAvailable = availableSizes?.contains(it) ?: false
                )
            } ?: emptyList(),
            images = images.map(::Image)
        )
}
