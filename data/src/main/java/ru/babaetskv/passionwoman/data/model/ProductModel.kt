package ru.babaetskv.passionwoman.data.model

import com.squareup.moshi.Json
import ru.babaetskv.passionwoman.domain.model.Image
import ru.babaetskv.passionwoman.domain.model.Price
import ru.babaetskv.passionwoman.domain.model.Product
import ru.babaetskv.passionwoman.domain.model.base.Transformable
import ru.babaetskv.passionwoman.domain.utils.transform
import ru.babaetskv.passionwoman.domain.utils.transformList

data class ProductModel(
    @Json(name = "id") val id: String,
    @Json(name = "category") val category: CategoryModel,
    @Json(name = "name") val name: String,
    @Json(name = "description") val description: String?,
    @Json(name = "preview") val preview: String,
    @Json(name = "price") val price: Float,
    @Json(name = "priceWithDiscount") val priceWithDiscount: Float,
    @Json(name = "rating") val rating: Float,
    @Json(name = "brand") val brand: BrandModel?,
    @Json(name = "model") val model: String?,
    @Json(name = "colors") val colors: List<ProductColorModel>
) : Transformable<Unit, Product> {

    override fun transform(params: Unit): Product =
        Product(
            id = id,
            category = category.transform(),
            name = name,
            description = description,
            preview = Image(preview),
            price = Price(price),
            priceWithDiscount = Price(priceWithDiscount),
            rating = rating,
            brand = brand?.transform(),
            colors = colors.transformList()
        )
}
