package ru.babaetskv.passionwoman.domain.gateway.exception

import ru.babaetskv.passionwoman.domain.exceptions.GatewayException

interface GatewayExceptionProvider {
    fun getGatewayException(e: Exception): GatewayException
}
