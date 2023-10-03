package ru.babaetskv.passionwoman.app.push

import android.os.Bundle
import ru.babaetskv.passionwoman.app.utils.deeplink.DeeplinkPayload

class AppNotificationDataConverterImpl : AppNotificationDataConverter {

    override suspend fun convert(bundle: Bundle): DeeplinkPayload? {
        val type: AppNotificationType =
            bundle.getString(KEY_TYPE)?.let(AppNotificationType::getByType) ?: return null

        return when (type) {
            AppNotificationType.ORDER -> {
                val id = bundle.getString(KEY_ID)?.toLongOrNull() ?: return null

                DeeplinkPayload.Order(id)
            }
            AppNotificationType.PRODUCT -> {
                val id = bundle.getString(KEY_ID)?.toLongOrNull() ?: return null

                DeeplinkPayload.Product(id)
            }
        }
    }

    companion object {
        private const val KEY_TYPE = "type"
        private const val KEY_ID = "id"
    }
}
