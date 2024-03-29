package ru.babaetskv.passionwoman.data.model

import com.squareup.moshi.Json
import ru.babaetskv.passionwoman.domain.model.Image
import ru.babaetskv.passionwoman.domain.model.Profile
import ru.babaetskv.passionwoman.domain.model.base.Transformable

data class ProfileModel(
    @Json(name = "id") val id: Long,
    @Json(name = "name") val name: String,
    @Json(name = "surname") val surname: String,
    @Json(name = "phone") val phone: String,
    @Json(name = "avatar") val avatar: String?
) : Transformable<Unit, Profile>() {

    constructor(profile: Profile) :
        this(
            id = profile.id,
            name = profile.name,
            surname = profile.surname,
            phone = profile.phone,
            avatar = profile.avatar?.toString()
        )

    override suspend fun transform(params: Unit): Profile =
        Profile(
            id = id,
            name = name,
            surname = surname,
            phone = phone,
            avatar = avatar?.let(::Image)
        )
}
