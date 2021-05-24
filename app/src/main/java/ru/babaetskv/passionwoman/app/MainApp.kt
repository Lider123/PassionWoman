package ru.babaetskv.passionwoman.app

import android.app.Application
import com.chibatching.kotpref.Kotpref
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import ru.babaetskv.passionwoman.app.di.*

class MainApp : Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin()
        initKotpref()
    }

    private fun initKotpref() {
        Kotpref.init(this)
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
                gatewayModule
            )
        }

    }
}
