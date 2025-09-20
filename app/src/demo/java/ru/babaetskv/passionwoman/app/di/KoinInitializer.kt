package ru.babaetskv.passionwoman.app.di

import android.content.Context
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import ru.babaetskv.passionwoman.app.BuildConfig

object KoinInitializer {
    var isInitialized: Boolean = false
        private set

    fun init(appContext: Context) {
        startKoin {
            androidLogger(if (BuildConfig.DEBUG) Level.ERROR else Level.NONE)
            androidContext(appContext)
            modules(
                appModule,
                networkModule,
                navigationModule,
                interactorModule,
                viewModelModule,
                preferencesModule,
                gatewayModule,
                dataFlowModule,
                assetsModule,
                demoModule
            )
        }
        isInitialized = true
    }
}
