package ru.babaetskv.passionwoman.app.analytics

import android.app.Application
import androidx.core.os.bundleOf
import com.google.firebase.analytics.FirebaseAnalytics
import ru.babaetskv.passionwoman.app.BuildConfig

class GoogleAnalyticService : AnalyticService {
    private lateinit var analytics: FirebaseAnalytics

    override val isEnabled: Boolean = BuildConfig.ANALYTICS_ENABLED

    override fun init(app: Application) {
        analytics = FirebaseAnalytics.getInstance(app).apply {
            setAnalyticsCollectionEnabled(isEnabled)
        }
    }

    override fun log(event: AnalyticEvent) {
        analytics.logEvent(event.name, bundleOf())
    }
}
