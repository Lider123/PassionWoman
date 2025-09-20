package ru.babaetskv.passionwoman.domain.gateway

interface AuthGateway {
    suspend fun authorize(accessToken: String): String
}
