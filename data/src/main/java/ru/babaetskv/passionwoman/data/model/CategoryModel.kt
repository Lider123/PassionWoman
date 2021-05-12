package ru.babaetskv.passionwoman.data.model

import com.squareup.moshi.Json
import ru.babaetskv.passionwoman.domain.model.Category
import ru.babaetskv.passionwoman.domain.model.Image

data class CategoryModel(
    @Json(name = "id") val id: String,
    @Json(name = "name") val name: String,
    @Json(name = "image") val image: String,
) {

    fun toCategory() =
        Category(
            id = id,
            name = name,
            image = Image(image)
        )
}
