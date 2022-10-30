package ru.babaetskv.passionwoman.data.gateway.decorator

import ru.babaetskv.passionwoman.domain.gateway.exception.GatewayExceptionProvider
import ru.babaetskv.passionwoman.domain.gateway.AuthGateway

class SafeAuthGatewayDecorator(
    private val gateway: AuthGateway,
    private val exceptionProvider: GatewayExceptionProvider
) : AuthGateway {

    override suspend fun authorize(accessToken: String): String =
        try {
            gateway.authorize(accessToken)
        } catch (e: Exception) {
            throw exceptionProvider.getGatewayException(e)
        }
}
