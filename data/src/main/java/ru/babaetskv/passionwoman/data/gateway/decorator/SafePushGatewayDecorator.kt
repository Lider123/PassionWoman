package ru.babaetskv.passionwoman.data.gateway.decorator

import ru.babaetskv.passionwoman.data.gateway.decorator.base.PushGatewayDecorator
import ru.babaetskv.passionwoman.domain.gateway.PushGateway
import ru.babaetskv.passionwoman.domain.gateway.exception.GatewayExceptionProvider

class SafePushGatewayDecorator(
    gateway: PushGateway,
    private val exceptionProvider: GatewayExceptionProvider
) : PushGatewayDecorator(gateway) {

    override suspend fun registerPushToken(token: String) {
        try {
            super.registerPushToken(token)
        } catch (e: Exception) {
            throw exceptionProvider.getGatewayException(e)
        }
    }

    override suspend fun unregisterPushToken(token: String) {
        try {
            super.unregisterPushToken(token)
        } catch (e: Exception) {
            throw exceptionProvider.getGatewayException(e)
        }
    }
}
