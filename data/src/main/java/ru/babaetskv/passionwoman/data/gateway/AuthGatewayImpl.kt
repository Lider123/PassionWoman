package ru.babaetskv.passionwoman.data.gateway

import ru.babaetskv.passionwoman.data.api.CommonApi
import ru.babaetskv.passionwoman.data.model.AccessTokenModel
import ru.babaetskv.passionwoman.domain.gateway.AuthGateway

class AuthGatewayImpl(
    private val api: CommonApi
) : AuthGateway {

    override suspend fun authorize(accessToken: String): String =
        api.authorize(AccessTokenModel(accessToken)).token
}
