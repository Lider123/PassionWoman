package ru.babaetskv.passionwoman.data.model

import com.squareup.moshi.Json
import ru.babaetskv.passionwoman.domain.model.Brand
import ru.babaetskv.passionwoman.domain.model.Image

data class BrandModel(
    @Json(name = "id") val id: String,
    @Json(name = "logo") val logo: String,
    @Json(name = "name") val name: String
) {

    fun toBrand() =
        Brand(
            id = id,
            logo = Image(logo),
            name = name
        )
}
