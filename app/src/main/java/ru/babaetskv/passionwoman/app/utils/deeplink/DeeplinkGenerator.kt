package ru.babaetskv.passionwoman.app.utils.deeplink

import ru.babaetskv.passionwoman.domain.model.Product

interface DeeplinkGenerator {
    suspend fun createProductDeeplink(product: Product) : String?

    companion object {
        const val PRODUCT_HOST = "showProduct"
        const val PRODUCT_PARAM_ID = "id"
    }
}
