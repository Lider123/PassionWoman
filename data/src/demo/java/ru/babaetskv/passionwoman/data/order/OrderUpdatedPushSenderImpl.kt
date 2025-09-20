package ru.babaetskv.passionwoman.data.order

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor
import ru.babaetskv.passionwoman.data.BuildConfig
import ru.babaetskv.passionwoman.domain.StringProvider
import ru.babaetskv.passionwoman.domain.model.Order
import ru.babaetskv.passionwoman.domain.preferences.AppPreferences
import ru.gildor.coroutines.okhttp.await

class OrderUpdatedPushSenderImpl(
    private val moshi: Moshi,
    private val appPreferences: AppPreferences,
    private val stringProvider: StringProvider
) : OrderUpdatedPushSender {

    override suspend fun send(orderId: Long, newStatus: Order.Status) =
        withContext(Dispatchers.IO) {
            val title = "Order status updated"
            val body = stringProvider.getOrderNotificationBody(orderId, newStatus)
            val data = mapOf(
                "to" to appPreferences.pushToken,
                "notification" to mapOf(
                    "body" to body,
                    "title" to title
                ),
                "data" to mapOf(
                    "body" to body,
                    "title" to title,
                    "type" to "order",
                    "id" to orderId.toString()
                )
            )
            val jsonAdapter: JsonAdapter<Map<String, Any>> =
                moshi.adapter(Types.newParameterizedType(Map::class.java, String::class.java, Any::class.java))
            val json = jsonAdapter.indent(" ")
                .toJson(data)
            val client = OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().apply {
                    level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
                })
                .build()
            val requestBody = RequestBody.create("application/json".toMediaType(), json)
            val request = Request.Builder()
                .url(FCM_URL)
                .header("Authorization", "key=${BuildConfig.FCM_KEY}")
                .post(requestBody)
                .build()
            client.newCall(request)
                .await()
            return@withContext
        }

    companion object {
        private const val FCM_URL = "https://fcm.googleapis.com/fcm/send"
    }
}
