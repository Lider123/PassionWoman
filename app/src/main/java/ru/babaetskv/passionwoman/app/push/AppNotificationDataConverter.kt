package ru.babaetskv.passionwoman.app.push

import android.os.Bundle
import ru.babaetskv.passionwoman.app.utils.deeplink.DeeplinkPayload

interface AppNotificationDataConverter {

    suspend fun convert(bundle: Bundle): DeeplinkPayload?
}
