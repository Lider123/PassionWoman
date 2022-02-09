package ru.babaetskv.passionwoman.data.model

import com.squareup.moshi.Json
import ru.babaetskv.passionwoman.domain.model.Image
import ru.babaetskv.passionwoman.domain.model.Promotion
import ru.babaetskv.passionwoman.domain.model.base.Transformable

data class PromotionModel(
    @Json(name = "id") val id: String,
    @Json(name = "image") val banner: String
) : Transformable<Unit, Promotion> {

    override fun transform(params: Unit): Promotion =
        Promotion(
            id = id,
            banner = Image(banner)
        )
}
