package ru.babaetskv.passionwoman.data.model

import com.squareup.moshi.Json
import ru.babaetskv.passionwoman.domain.model.Brand
import ru.babaetskv.passionwoman.domain.model.Image
import ru.babaetskv.passionwoman.domain.model.base.Transformable

data class BrandModel(
    @Json(name = "id") val id: Int,
    @Json(name = "logo_path") val logoPath: String,
    @Json(name = "name") val name: String
) : Transformable<Unit, Brand> {

    override suspend fun transform(params: Unit): Brand =
        Brand(
            id = id,
            logo = Image(logoPath),
            name = name
        )
}
