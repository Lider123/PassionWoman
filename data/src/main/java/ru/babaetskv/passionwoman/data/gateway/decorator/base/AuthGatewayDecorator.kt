package ru.babaetskv.passionwoman.data.gateway.decorator.base

import ru.babaetskv.passionwoman.domain.gateway.AuthGateway

abstract class AuthGatewayDecorator(
    val gateway: AuthGateway
) : AuthGateway {

    override suspend fun authorize(accessToken: String): String = gateway.authorize(accessToken)
}
