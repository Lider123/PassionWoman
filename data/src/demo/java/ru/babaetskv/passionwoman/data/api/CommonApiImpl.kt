package ru.babaetskv.passionwoman.data.api

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import ru.babaetskv.passionwoman.data.model.AccessTokenModel
import ru.babaetskv.passionwoman.data.model.AuthTokenModel

class CommonApiImpl : CommonApi {

    override suspend fun authorize(body: AccessTokenModel): AuthTokenModel =
        withContext(Dispatchers.IO) {
            delay(DELAY_LOADING)
            return@withContext AuthTokenModel("token")
        }

    companion object {
        private const val DELAY_LOADING = 500L
    }
}
