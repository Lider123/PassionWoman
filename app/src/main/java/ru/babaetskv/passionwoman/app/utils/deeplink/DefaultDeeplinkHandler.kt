package ru.babaetskv.passionwoman.app.utils.deeplink

import android.net.Uri

class DefaultDeeplinkHandler : DeeplinkHandler {

    override suspend fun handle(deeplink: Uri?): DeeplinkPayload? =
        when (deeplink?.path?.trimStart('/')) {
            DeeplinkGenerator.PRODUCT_PATH -> handleProductDeeplink(deeplink)
            DeeplinkGenerator.SEARCH_PATH -> handleSearchDeeplink(deeplink)
            else -> null
        }

    private fun handleProductDeeplink(deeplink: Uri): DeeplinkPayload? {
        val id = deeplink.getQueryParameter(DeeplinkGenerator.PRODUCT_PARAM_ID)
            ?.toLongOrNull()
            ?: return null

        return DeeplinkPayload.Product(id)
    }

    private fun handleSearchDeeplink(deeplink: Uri): DeeplinkPayload? = DeeplinkPayload.Search
}
