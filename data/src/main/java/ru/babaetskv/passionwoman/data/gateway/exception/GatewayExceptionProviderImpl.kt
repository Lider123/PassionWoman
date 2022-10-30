package ru.babaetskv.passionwoman.data.gateway.exception

import retrofit2.HttpException
import ru.babaetskv.passionwoman.domain.StringProvider
import ru.babaetskv.passionwoman.domain.exceptions.GatewayException
import ru.babaetskv.passionwoman.domain.gateway.exception.GatewayExceptionProvider

class GatewayExceptionProviderImpl(
    private val stringProvider: StringProvider
) : GatewayExceptionProvider {

    override fun getGatewayException(e: Exception): GatewayException {
        if (e !is HttpException) return GatewayException.Unknown(e, stringProvider)

        return when {
            e.code() == 401 -> GatewayException.Unauthorized(e, stringProvider)
            e.code() / 100 == 4 -> GatewayException.Client(e, stringProvider)
            e.code() / 100 == 5 -> GatewayException.Server(e, stringProvider)
            else -> GatewayException.Unknown(e, stringProvider)
        }
    }
}
