package ru.babaetskv.passionwoman.data.gateway

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import ru.babaetskv.passionwoman.data.api.AuthApi
import ru.babaetskv.passionwoman.domain.gateway.PushGateway

class PushGatewayImpl(
    private val api: AuthApi
) : PushGateway {

    override suspend fun registerPushToken(token: String) = withContext(Dispatchers.IO) {
        api.registerPushToken(token.toPart())
    }

    override suspend fun unregisterPushToken(token: String) = withContext(Dispatchers.IO) {
        api.unregisterPushToken(token.toPart())
    }

    private fun String.toPart(): MultipartBody.Part =
        this.toRequestBody("text/plain".toMediaTypeOrNull())
            .let(MultipartBody.Part::create)
}
