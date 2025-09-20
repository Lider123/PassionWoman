package ru.babaetskv.passionwoman.app.utils.deeplink

import android.net.Uri
import ru.babaetskv.passionwoman.domain.model.Product

class DeeplinkGeneratorImpl : DeeplinkGenerator {

    override suspend fun createProductDeeplink(product: Product): String =
        Uri.Builder()
            .scheme(DeeplinkGenerator.URL_SCHEMA)
            .authority(DeeplinkGenerator.URL_HOST)
            .appendPath(DeeplinkGenerator.PRODUCT_PATH)
            .appendQueryParameter(DeeplinkGenerator.PRODUCT_PARAM_ID, product.id.toString())
            .build()
            .toString()
}
