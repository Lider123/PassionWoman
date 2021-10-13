package ru.babaetskv.passionwoman.data.model

import com.squareup.moshi.Json
import ru.babaetskv.passionwoman.domain.model.ProductsPagedResponse

data class ProductsPagedResponseModel(
    @Json(name = "products") val products: List<ProductModel>,
    @Json(name = "total") val total: Int
) {

    fun toProductPagedResponse() =
        ProductsPagedResponse(
            products = products.map(ProductModel::toProduct),
            total = total
        )
}
