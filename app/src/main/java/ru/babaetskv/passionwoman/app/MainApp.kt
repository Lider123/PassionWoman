package ru.babaetskv.passionwoman.app

import android.app.Application
import com.chibatching.kotpref.Kotpref
import ru.babaetskv.passionwoman.app.di.*

class MainApp : Application() {
    private val koinInitializer: KoinInitializer = KoinInitializerImpl()

    override fun onCreate() {
        super.onCreate()
        initKoin()
        initKotpref()
    }

    private fun initKotpref() {
        Kotpref.init(this)
    }

    private fun initKoin() {
        koinInitializer.init(this)
    }
}
