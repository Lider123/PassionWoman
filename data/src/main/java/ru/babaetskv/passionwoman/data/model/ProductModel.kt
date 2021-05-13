package ru.babaetskv.passionwoman.data.model

import com.squareup.moshi.Json
import ru.babaetskv.passionwoman.domain.model.Image
import ru.babaetskv.passionwoman.domain.model.Product
import ru.babaetskv.passionwoman.domain.model.ProductItem

data class ProductModel(
    @Json(name = "name") val name: String,
    @Json(name = "preview") val preview: String,
    @Json(name = "items") val items: List<ProductItemModel>
) {

    fun toProduct() =
        Product(
            name = name,
            preview = Image(preview),
            items = items.map(ProductItemModel::toProductItem)
        )
}
