package ru.babaetskv.passionwoman.data.gateway.base

import kotlinx.coroutines.withContext
import retrofit2.HttpException
import ru.babaetskv.passionwoman.domain.AppDispatchers
import ru.babaetskv.passionwoman.domain.exceptions.GatewayException
import ru.babaetskv.passionwoman.domain.StringProvider

abstract class BaseGatewayImpl(
    protected val stringProvider: StringProvider,
    protected val dispatchers: AppDispatchers
) {

    protected suspend fun <T> networkRequest(block: suspend () -> T): T =
        withContext(dispatchers.IO) {
            try {
                return@withContext block.invoke()
            } catch (e: Exception) {
                throw getGatewayException(e)
            }
        }

    @PublishedApi
    internal fun getGatewayException(e: java.lang.Exception): GatewayException {
        if (e !is HttpException) return GatewayException.Unknown(e, stringProvider)

        return when {
            e.code() == 401 -> GatewayException.Unauthorized(e, stringProvider)
            e.code() / 100 == 4 -> GatewayException.Client(e, stringProvider)
            e.code() / 100 == 5 -> GatewayException.Server(e, stringProvider)
            else -> GatewayException.Unknown(e, stringProvider)
        }
    }
}
