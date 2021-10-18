package ru.babaetskv.passionwoman.app.analytics

import android.app.Application
import androidx.core.os.bundleOf
import com.huawei.hms.analytics.HiAnalytics
import com.huawei.hms.analytics.HiAnalyticsInstance
import com.huawei.hms.analytics.HiAnalyticsTools
import ru.babaetskv.passionwoman.app.BuildConfig

class HuaweiAnalyticsService : AnalyticService {
    private lateinit var analytics: HiAnalyticsInstance

    override val isEnabled: Boolean = BuildConfig.ANALYTICS_ENABLED

    override fun init(app: Application) {
        analytics = HiAnalytics.getInstance(app)
        if (isEnabled) HiAnalyticsTools.enableLog()
    }

    override fun log(event: AnalyticEvent) {
        analytics.onEvent(event.name, bundleOf())
    }
}
