package ru.babaetskv.passionwoman.app.utils.deeplink

import android.net.Uri
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import ru.babaetskv.passionwoman.domain.model.Product

class FirebaseDeeplinkGenerator : DeeplinkGenerator {

    override suspend fun createProductDeeplink(product: Product): String? {
        val url = createProductUrl(product)
        return Firebase.dynamicLinks.createDynamicLink()
            .setDomainUriPrefix(URI_PREFIX)
            .setLink(Uri.parse(url))
            .buildShortDynamicLink()
            .await()
            .shortLink
            ?.toString()
    }

    private fun createProductUrl(product: Product): String =
        "$URL_SCHEMA://${DeeplinkGenerator.PRODUCT_HOST}?${DeeplinkGenerator.PRODUCT_PARAM_ID}=${product.id}"

    companion object {
        private const val URI_PREFIX = "https://passionwoman.page.link"
        private const val URL_SCHEMA = "https"
    }
}
