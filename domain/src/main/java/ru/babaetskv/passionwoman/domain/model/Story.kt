package ru.babaetskv.passionwoman.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Story(
    val id: String,
    val banner: Image,
    val contents: List<Content>
) : Parcelable {

    sealed class Content : Parcelable {
        abstract val id: String
        abstract val title: String?
        abstract val text: String?
        // TODO: add deeplink to promotion

        @Parcelize
        data class Video(
            override val id: String,
            override val title: String?,
            override val text: String?,
            val video: ru.babaetskv.passionwoman.domain.model.Video
        ) : Content()

        @Parcelize
        data class Image(
            override val id: String,
            override val title: String?,
            override val text: String?,
            val image: ru.babaetskv.passionwoman.domain.model.Image
        ) : Content()
    }
}