package ru.babaetskv.passionwoman.data.model

import com.squareup.moshi.Json
import ru.babaetskv.passionwoman.domain.model.Image
import ru.babaetskv.passionwoman.domain.model.Story
import ru.babaetskv.passionwoman.domain.model.Video
import ru.babaetskv.passionwoman.domain.model.base.Transformable
import ru.babaetskv.passionwoman.domain.utils.transformListNotNull

data class StoryModel(
    @Json(name = "id") val id: String,
    @Json(name = "image") val banner: String,
    @Json(name = "contents") val contents: List<ContentModel>
) : Transformable<Unit, Story> {

    override suspend fun transform(params: Unit): Story =
        Story(
            id = id,
            banner = Image(banner),
            contents = contents.transformListNotNull()
        )

    data class ContentModel(
        @Json(name = "id") val id: String,
        @Json(name = "title") val title: String?,
        @Json(name = "text") val text: String?,
        @Json(name = "media") val media: String,
        @Json(name = "type") val type: String
    ) : Transformable<Unit, Story.Content?> {

        override suspend fun transform(params: Unit): Story.Content? =
            Type.findByApiName(type)?.let {
                when (it) {
                    Type.IMAGE -> {
                        Story.Content.Image(
                            id = id,
                            title = title,
                            text = text,
                            image = Image(media)
                        )
                    }
                    Type.VIDEO -> {
                        Story.Content.Video(
                            id = id,
                            title = title,
                            text = text,
                            video = Video(media)
                        )
                    }
                }
            }

        enum class Type(
            private val apiName: String
        ) {
            IMAGE("image"),
            VIDEO("video");

            companion object {

                fun findByApiName(apiName: String): Type? = values().find { it.apiName == apiName }
            }
        }
    }
}
