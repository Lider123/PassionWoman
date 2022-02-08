package ru.babaetskv.passionwoman.data.model

import com.squareup.moshi.Json
import ru.babaetskv.passionwoman.domain.model.Color
import ru.babaetskv.passionwoman.domain.model.base.Transformable

data class ColorModel(
    @Json(name = "code") val code: String,
    @Json(name = "uiName") val uiName: String,
    @Json(name = "hex") val hex: String
) : Transformable<Unit, Color> {

    override fun transform(params: Unit): Color =
        Color(
            code = code,
            uiName = uiName,
            hex = hex
        )
}
