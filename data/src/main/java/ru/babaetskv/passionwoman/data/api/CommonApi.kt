package ru.babaetskv.passionwoman.data.api

import retrofit2.http.Body
import retrofit2.http.POST
import ru.babaetskv.passionwoman.data.model.AccessTokenModel
import ru.babaetskv.passionwoman.data.model.AuthTokenModel

interface CommonApi {

    @POST("api/auth/authorize")
    suspend fun authorize(
        @Body body: AccessTokenModel
    ): AuthTokenModel
}
