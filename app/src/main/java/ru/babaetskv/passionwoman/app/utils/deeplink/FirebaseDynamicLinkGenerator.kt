package ru.babaetskv.passionwoman.app.utils.deeplink

import androidx.core.net.toUri
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import com.google.firebase.dynamiclinks.ktx.androidParameters
import com.google.firebase.dynamiclinks.ktx.socialMetaTagParameters
import ru.babaetskv.passionwoman.domain.model.Product

class FirebaseDynamicLinkGenerator(
    private val deeplinkGenerator: DeeplinkGenerator
) : ExternalDeeplinkGenerator {

    override suspend fun createProductDeeplink(product: Product): String? =
        FirebaseDynamicLinks.getInstance()
            .createDynamicLink()
            .run {
                link = deeplinkGenerator.createProductDeeplink(product)?.toUri() ?: return@run null

                domainUriPrefix = ExternalDeeplinkGenerator.URI_PREFIX
                socialMetaTagParameters {
                    title = product.name
                    product.description?.let(::setDescription)
                }
                androidParameters {
                    build()
                }
                buildDynamicLink()
            }
            ?.uri
            ?.toString()
}
