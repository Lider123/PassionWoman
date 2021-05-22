package ru.babaetskv.passionwoman.app

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import ru.babaetskv.passionwoman.app.di.*

class MainApp : Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin()
    }

    private fun initKoin() {
        startKoin {
            if (BuildConfig.DEBUG) androidLogger()
            androidContext(this@MainApp)
            modules(
                appModule,
                networkModule,
                navigationModule,
                interactorModule,
                viewModelModule,
                preferencesModule,
                repositoryModule
            )
        }

    }
}
