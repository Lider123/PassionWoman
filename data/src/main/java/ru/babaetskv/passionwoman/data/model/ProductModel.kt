package ru.babaetskv.passionwoman.data.model

import com.squareup.moshi.Json
import ru.babaetskv.passionwoman.domain.model.Image
import ru.babaetskv.passionwoman.domain.model.Product

data class ProductModel(
    @Json(name = "name") val name: String,
    @Json(name = "preview") val preview: String
) {

    fun toProduct() =
        Product(
            name = name,
            preview = Image(preview)
        )
}
