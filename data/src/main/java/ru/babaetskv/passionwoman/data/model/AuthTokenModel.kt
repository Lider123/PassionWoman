package ru.babaetskv.passionwoman.data.model

import com.squareup.moshi.Json

data class AuthTokenModel(
    @Json(name = "token") val token: String
)
