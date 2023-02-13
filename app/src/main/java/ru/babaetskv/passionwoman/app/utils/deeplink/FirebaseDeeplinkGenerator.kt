package ru.babaetskv.passionwoman.app.utils.deeplink

import android.net.Uri
import androidx.core.net.toUri
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import com.google.firebase.dynamiclinks.ktx.androidParameters
import com.google.firebase.dynamiclinks.ktx.socialMetaTagParameters
import ru.babaetskv.passionwoman.domain.model.Product

class FirebaseDeeplinkGenerator : DeeplinkGenerator {

    override suspend fun createProductDeeplink(product: Product): String =
        FirebaseDynamicLinks.getInstance()
            .createDynamicLink()
            .run {
                link = createProductUrl(product)
                domainUriPrefix = URI_PREFIX
                socialMetaTagParameters {
                    title = product.name
                    product.description?.let(::setDescription)
                }
                androidParameters {
                    build()
                }
                buildDynamicLink()
            }
            .uri
            .toString()

    private fun createProductUrl(product: Product): Uri =
        "$URL_SCHEMA://${DeeplinkGenerator.PRODUCT_HOST}?${DeeplinkGenerator.PRODUCT_PARAM_ID}=${product.id}"
            .toUri()

    companion object {
        private const val URI_PREFIX = "https://passionwoman.page.link"
        private const val URL_SCHEMA = "https"
    }
}
