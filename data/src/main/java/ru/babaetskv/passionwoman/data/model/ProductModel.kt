package ru.babaetskv.passionwoman.data.model

import com.squareup.moshi.Json
import ru.babaetskv.passionwoman.domain.model.Image
import ru.babaetskv.passionwoman.domain.model.Product

data class ProductModel(
    @Json(name = "id") val id: String,
    @Json(name = "name") val name: String,
    @Json(name = "description") val description: String?,
    @Json(name = "preview") val preview: String,
    @Json(name = "price") val price: Float,
    @Json(name = "priceWithDiscount") val priceWithDiscount: Float,
    @Json(name = "rating") val rating: Float,
    @Json(name = "brand") val brand: BrandModel?,
    @Json(name = "colors") val colors: List<ProductColorModel>
) {

    fun toProduct() =
        Product(
            id = id,
            name = name,
            description = description,
            preview = Image(preview),
            price = price,
            priceWithDiscount = priceWithDiscount,
            rating = rating,
            brand = brand?.toBrand(),
            colors = colors.map(ProductColorModel::toProductColor)
        )
}
