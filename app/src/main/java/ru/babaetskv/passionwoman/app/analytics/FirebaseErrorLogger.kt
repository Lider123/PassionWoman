package ru.babaetskv.passionwoman.app.analytics

import android.os.Build
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.crashlytics.ktx.setCustomKeys
import com.google.firebase.ktx.Firebase
import ru.babaetskv.passionwoman.app.BuildConfig
import ru.babaetskv.passionwoman.app.analytics.base.ErrorLogger
import ru.babaetskv.passionwoman.domain.preferences.AuthPreferences

class FirebaseErrorLogger(authPreferences: AuthPreferences) : ErrorLogger {

    init {
        Firebase.crashlytics.setCrashlyticsCollectionEnabled(!BuildConfig.DEBUG)
        Firebase.crashlytics.setCustomKeys {
            key(CrashKeys.VERSION_NAME, BuildConfig.VERSION_NAME)
            key(CrashKeys.API_LEVEL, Build.VERSION.SDK_INT)
            key(CrashKeys.DEVICE, Build.DEVICE)
            key(CrashKeys.MODEL, Build.MODEL)
            key(CrashKeys.PRODUCT, Build.PRODUCT)
        }
        authPreferences.observeUserId(::onUserIdChanged)
    }

    override fun logException(t: Throwable) {
        Firebase.crashlytics.recordException(t)
    }

    private fun onUserIdChanged(userId: Int) {
        Firebase.crashlytics.setUserId(userId.toString())
    }

    private object CrashKeys {
        const val VERSION_NAME: String = "version_name"
        const val API_LEVEL: String = "api_level"
        const val DEVICE: String = "device"
        const val MODEL: String = "model"
        const val PRODUCT: String = "product"
    }
}
