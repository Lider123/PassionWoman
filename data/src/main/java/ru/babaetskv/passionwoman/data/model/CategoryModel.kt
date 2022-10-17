package ru.babaetskv.passionwoman.data.model

import com.squareup.moshi.Json
import ru.babaetskv.passionwoman.domain.model.Category
import ru.babaetskv.passionwoman.domain.model.Image
import ru.babaetskv.passionwoman.domain.model.base.Transformable

data class CategoryModel(
    @Json(name = "id") val id: Long,
    @Json(name = "name") val name: String,
    @Json(name = "image") val image: String,
) : Transformable<Unit, Category>() {

    override suspend fun transform(params: Unit): Category =
        Category(
            id = id,
            name = name,
            image = Image(image)
        )
}
