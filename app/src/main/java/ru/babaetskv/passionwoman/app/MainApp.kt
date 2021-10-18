package ru.babaetskv.passionwoman.app

import android.app.Application
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import com.chibatching.kotpref.Kotpref
import org.koin.android.ext.android.inject
import ru.babaetskv.passionwoman.app.analytics.AnalyticService
import ru.babaetskv.passionwoman.app.di.*

class MainApp : Application() {
    private val koinInitializer: KoinInitializer = KoinInitializerImpl()
    private val analyticService: AnalyticService by inject()

    override fun onCreate() {
        super.onCreate()
        initKoin()
        initKotpref()
        initAnalytics()
        if (Build.VERSION.SDK_INT < 29) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
    }

    private fun initKotpref() {
        Kotpref.init(this)
    }

    private fun initKoin() {
        koinInitializer.init(this)
    }

    private fun initAnalytics() {
        analyticService.init(this)
    }
}
