package ru.babaetskv.passionwoman.data.model

import com.squareup.moshi.Json
import ru.babaetskv.passionwoman.domain.model.Color

data class ColorModel(
    @Json(name = "name") val name: String,
    @Json(name = "hex") val hex: String
) {

    fun toColor() =
        Color(
            name = name,
            hex = hex
        )
}
