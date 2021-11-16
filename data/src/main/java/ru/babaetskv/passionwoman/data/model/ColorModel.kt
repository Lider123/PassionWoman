package ru.babaetskv.passionwoman.data.model

import com.squareup.moshi.Json
import ru.babaetskv.passionwoman.domain.model.Color

data class ColorModel(
    @Json(name = "code") val code: String,
    @Json(name = "uiName") val uiName: String,
    @Json(name = "hex") val hex: String
) {

    fun toColor() =
        Color(
            code = code,
            uiName = uiName,
            hex = hex
        )
}
