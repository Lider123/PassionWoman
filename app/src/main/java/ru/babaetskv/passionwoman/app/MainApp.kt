package ru.babaetskv.passionwoman.app

import android.app.Application
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import androidx.startup.AppInitializer
import androidx.work.Configuration
import androidx.work.WorkerFactory
import com.chibatching.kotpref.Kotpref
import net.danlew.android.joda.JodaTimeInitializer
import org.koin.android.ext.android.inject
import ru.babaetskv.passionwoman.app.di.*
import timber.log.Timber

class MainApp : Application(), Configuration.Provider {
    private val workerFactory: WorkerFactory by inject()

    override fun onCreate() {
        super.onCreate()
        initTimber()
        if (!KoinInitializer.isInitialized) KoinInitializer.init(this)
        initKotpref()
        initJoda()
        if (Build.VERSION.SDK_INT < 29) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
    }

    override fun getWorkManagerConfiguration(): Configuration {
        if (!KoinInitializer.isInitialized) KoinInitializer.init(this)
        return Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
    }

    private fun initTimber() {
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
    }

    private fun initKotpref() {
        Kotpref.init(this)
    }

    private fun initJoda() {
        AppInitializer.getInstance(this)
            .initializeComponent(JodaTimeInitializer::class.java)
    }
}
