package ru.babaetskv.passionwoman.data.model

import com.squareup.moshi.Json
import ru.babaetskv.passionwoman.domain.model.Image
import ru.babaetskv.passionwoman.domain.model.Price
import ru.babaetskv.passionwoman.domain.model.Product
import ru.babaetskv.passionwoman.domain.model.base.Transformable

data class ProductModel(
    @Json(name = "id") val id: Long,
    @Json(name = "category") val category: CategoryModel,
    @Json(name = "name") val name: String,
    @Json(name = "description") val description: String?,
    @Json(name = "preview") val preview: String,
    @Json(name = "price") val price: Float,
    @Json(name = "priceWithDiscount") val priceWithDiscount: Float,
    @Json(name = "createdAt") val createdAt: Long,
    @Json(name = "rating") val rating: Float,
    @Json(name = "brand") val brand: BrandModel?,
    @Json(name = "additional_info") val additionalInfo: Map<String, List<String>>?,
    @Json(name = "items") val items: List<ProductItemModel>
) : Transformable<Unit, Product>() {

    override suspend fun transform(params: Unit): Product =
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
            additionalInfo = additionalInfo,
            items = items.transformList()
        )
}
