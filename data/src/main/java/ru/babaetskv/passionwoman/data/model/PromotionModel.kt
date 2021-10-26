package ru.babaetskv.passionwoman.data.model

import com.squareup.moshi.Json
import ru.babaetskv.passionwoman.domain.model.Image
import ru.babaetskv.passionwoman.domain.model.Promotion

data class PromotionModel(
    @Json(name = "id") val id: String,
    @Json(name = "image") val banner: String
) {

    fun toPromotion() =
        Promotion(
            id = id,
            banner = Image(banner)
        )
}
