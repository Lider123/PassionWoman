package ru.babaetskv.passionwoman.app.utils.deeplink

import android.net.Uri

interface DeeplinkHandler {

    suspend fun handle(deeplink: Uri?): DeeplinkPayload?
}
