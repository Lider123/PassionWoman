package ru.babaetskv.passionwoman.domain.gateway

interface PushGateway {

    suspend fun registerPushToken(token: String)

    suspend fun unregisterPushToken(token: String)
}
