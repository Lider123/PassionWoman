package ru.babaetskv.passionwoman.data.gateway.decorator.base

import ru.babaetskv.passionwoman.domain.gateway.PushGateway

abstract class PushGatewayDecorator(
    val gateway: PushGateway
) : PushGateway {

    override suspend fun registerPushToken(token: String) {
        gateway.registerPushToken(token)
    }

    override suspend fun unregisterPushToken(token: String) {
        gateway.unregisterPushToken(token)
    }
}
