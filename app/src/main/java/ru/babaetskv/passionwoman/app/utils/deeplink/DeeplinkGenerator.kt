package ru.babaetskv.passionwoman.app.utils.deeplink

import ru.babaetskv.passionwoman.domain.model.Product

interface DeeplinkGenerator {
    suspend fun createProductDeeplink(product: Product) : String?

    companion object {
        const val URL_SCHEMA = "https"
        const val URL_HOST = "passionwoman-d63c4.web.app"
        const val PRODUCT_PATH = "product"
        const val SEARCH_PATH = "search"
        const val PRODUCT_PARAM_ID = "id"
        const val URI_PREFIX = "$URL_SCHEMA://$URL_HOST"
    }
}
