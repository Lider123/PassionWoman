package ru.babaetskv.passionwoman.app.utils.deeplink

import android.net.Uri

class DeeplinkHandlerImpl(
    private val defaultDeeplinkHandler: DeeplinkHandler,
    private val externalDeeplinkHandler: DeeplinkHandler,
) : DeeplinkHandler {

    override suspend fun handle(deeplink: Uri?): DeeplinkPayload? {
        deeplink ?: return null

        if (deeplink.toString().startsWith(ExternalDeeplinkGenerator.URI_PREFIX)) {
            return externalDeeplinkHandler.handle(deeplink)
        }

        if (deeplink.toString().startsWith(DeeplinkGenerator.URI_PREFIX)) {
            return defaultDeeplinkHandler.handle(deeplink)
        }

        return null
    }
}
