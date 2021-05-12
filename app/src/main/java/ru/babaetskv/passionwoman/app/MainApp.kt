package ru.babaetskv.passionwoman.app

import android.app.Application
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import ru.babaetskv.passionwoman.app.di.*

class MainApp : Application(), KodeinAware {
    override val kodein = Kodein.lazy {
        import(appModule)
        import(navigationModule)
        import(viewModelModule)
        import(interactorModule)
        import(repositoryModule)
        import(networkModule)
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        lateinit var instance: MainApp
    }
}
