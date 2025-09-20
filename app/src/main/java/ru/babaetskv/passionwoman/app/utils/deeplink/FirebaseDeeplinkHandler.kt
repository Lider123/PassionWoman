package ru.babaetskv.passionwoman.app.utils.deeplink

import android.net.Uri
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import kotlinx.coroutines.tasks.await

class FirebaseDeeplinkHandler(
    private val deeplinkHandler: DeeplinkHandler
) : DeeplinkHandler {

    override suspend fun handle(deeplink: Uri?): DeeplinkPayload? {
        deeplink ?: return null

        val url = FirebaseDynamicLinks.getInstance()
            .getDynamicLink(deeplink)
            .await()
            .link ?: return null

        return deeplinkHandler.handle(url)
    }
}
