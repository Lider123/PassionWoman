package ru.babaetskv.passionwoman.app.utils.deeplink

import android.net.Uri
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import kotlinx.coroutines.tasks.await

class FirebaseDeeplinkHandler : DeeplinkHandler {

    override suspend fun handle(deeplink: Uri?): DeeplinkPayload? {
        deeplink ?: return null

        val url = FirebaseDynamicLinks.getInstance()
            .getDynamicLink(deeplink)
            .await()
            .link ?: return null

        return createPayloadFromUrl(url)
    }

    private fun createPayloadFromUrl(url: Uri): DeeplinkPayload? {
        return when (url.host) {
            DeeplinkGenerator.PRODUCT_HOST -> {
                url.getQueryParameter(DeeplinkGenerator.PRODUCT_PARAM_ID)?.let {
                    DeeplinkPayload.Product(it)
                }
            }
            else -> null
        }
    }
}
