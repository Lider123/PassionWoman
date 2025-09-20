package ru.babaetskv.passionwoman.domain.exceptions

import ru.babaetskv.passionwoman.domain.StringProvider

sealed class GatewayException(
    override val cause: Throwable,
    override val message: String
) : Exception() {

    class Unauthorized(
        cause: Exception,
        stringProvider: StringProvider
    ) : GatewayException(cause, stringProvider.UNAUTHORIZED_ERROR)

    class Client(
        cause: Exception,
        stringProvider: StringProvider
    ) : GatewayException(cause, stringProvider.CLIENT_ERROR)

    class Server(
        cause: Exception,
        stringProvider: StringProvider
    ) : GatewayException(cause, stringProvider.SERVER_ERROR)

    class Unknown(
        cause: Exception,
        stringProvider: StringProvider
    ) : GatewayException(cause, stringProvider.UNKNOWN_ERROR)
}
