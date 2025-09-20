package ru.babaetskv.passionwoman.data.model

import com.squareup.moshi.Json

data class AccessTokenModel(
    @Json(name = "access_token") val token: String?
)
