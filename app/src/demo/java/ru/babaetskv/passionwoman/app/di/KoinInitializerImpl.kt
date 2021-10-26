package ru.babaetskv.passionwoman.app.di

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import ru.babaetskv.passionwoman.app.BuildConfig

class KoinInitializerImpl : KoinInitializer {

    override fun init(app: Application) {
        startKoin {
            if (BuildConfig.DEBUG) androidLogger()
            androidContext(app)
            modules(
                appModule,
                networkModule,
                navigationModule,
                interactorModule,
                viewModelModule,
                preferencesModule,
                gatewayModule,
                dataSourceModule,
                demoModule
            )
        }
    }
}
