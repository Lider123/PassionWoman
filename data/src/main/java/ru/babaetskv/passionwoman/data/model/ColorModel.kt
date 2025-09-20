package ru.babaetskv.passionwoman.data.model

import com.squareup.moshi.Json
import ru.babaetskv.passionwoman.domain.model.Color
import ru.babaetskv.passionwoman.domain.model.base.Transformable

data class ColorModel(
    @Json(name = "id") val id: Long,
    @Json(name = "uiName") val uiName: String,
    @Json(name = "hex") val hex: String
) : Transformable<Unit, Color>() {

    constructor(color: Color) :
        this(
            id = color.id,
            uiName = color.uiName,
            hex = color.hex
        )

    override suspend fun transform(params: Unit): Color =
        Color(
            id = id,
            uiName = uiName,
            hex = hex
        )
}
