package ru.babaetskv.passionwoman.app.di

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Router
import org.kodein.di.Kodein
import org.kodein.di.generic.*
import ru.babaetskv.passionwoman.app.MainApp
import ru.babaetskv.passionwoman.app.presentation.base.ViewModelFactory
import ru.babaetskv.passionwoman.app.presentation.feature.catalog.CatalogViewModel
import ru.babaetskv.passionwoman.app.presentation.feature.splash.SplashViewModel
import ru.babaetskv.passionwoman.app.utils.viewModel
import ru.babaetskv.passionwoman.data.api.Api
import ru.babaetskv.passionwoman.data.api.ApiProvider
import ru.babaetskv.passionwoman.data.api.ApiProviderImpl
import ru.babaetskv.passionwoman.data.repository.CatalogRepositoryImpl
import ru.babaetskv.passionwoman.domain.interactor.GetCategoriesUseCase
import ru.babaetskv.passionwoman.domain.repository.CatalogRepository

val appModule = Kodein.Module("app") {
    bind<Context>() with provider { MainApp.instance.applicationContext }
}

val navigationModule = Kodein.Module("navigation") {
    bind<Cicerone<Router>>() with singleton { Cicerone.create() }
    bind<Router>() with singleton { instance<Cicerone<Router>>().router }
    bind<NavigatorHolder>() with singleton { instance<Cicerone<Router>>().getNavigatorHolder() }
}

val viewModelModule = Kodein.Module("viewModel") {
    bind<ViewModelProvider.Factory>() with singleton { ViewModelFactory(this.dkodein) }
    viewModel<SplashViewModel>() with provider {
        SplashViewModel(instance())
    }
    viewModel<CatalogViewModel>() with provider {
        CatalogViewModel(instance())
    }
}

val interactorModule = Kodein.Module("interactor") {
    bind<GetCategoriesUseCase>() with provider { GetCategoriesUseCase(instance()) }
}

val repositoryModule = Kodein.Module("repository") {
    bind<CatalogRepository>() with singleton { CatalogRepositoryImpl(instance()) }
}

val networkModule = Kodein.Module("network") {
    bind<ApiProvider>() with singleton { ApiProviderImpl(instance<Context>().assets) }
    bind<Api>() with singleton { instance<ApiProvider>().provideApi() }
}
