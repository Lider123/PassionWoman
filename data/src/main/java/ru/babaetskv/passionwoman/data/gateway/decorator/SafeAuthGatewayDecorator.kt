package ru.babaetskv.passionwoman.data.gateway.decorator

import ru.babaetskv.passionwoman.data.gateway.decorator.base.AuthGatewayDecorator
import ru.babaetskv.passionwoman.domain.gateway.exception.GatewayExceptionProvider
import ru.babaetskv.passionwoman.domain.gateway.AuthGateway

class SafeAuthGatewayDecorator(
    gateway: AuthGateway,
    private val exceptionProvider: GatewayExceptionProvider
) : AuthGatewayDecorator(gateway) {

    override suspend fun authorize(accessToken: String): String =
        try {
            super.authorize(accessToken)
        } catch (e: Exception) {
            throw exceptionProvider.getGatewayException(e)
        }
}
