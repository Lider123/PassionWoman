package ru.babaetskv.passionwoman.app

import android.app.Application
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import androidx.startup.AppInitializer
import com.chibatching.kotpref.Kotpref
import net.danlew.android.joda.JodaTimeInitializer
import ru.babaetskv.passionwoman.app.di.*
import timber.log.Timber

class MainApp : Application() {
    private val koinInitializer: KoinInitializer = KoinInitializerImpl()

    override fun onCreate() {
        super.onCreate()
        initTimber()
        initKoin()
        initKotpref()
        initJoda()
        if (Build.VERSION.SDK_INT < 29) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
    }

    private fun initTimber() {
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
    }

    private fun initKotpref() {
        Kotpref.init(this)
    }

    private fun initKoin() {
        koinInitializer.init(this)
    }

    private fun initJoda() {
        AppInitializer.getInstance(this)
            .initializeComponent(JodaTimeInitializer::class.java)
    }
}
