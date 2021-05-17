package ru.babaetskv.passionwoman.app.di

import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.Router
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.babaetskv.passionwoman.app.presentation.feature.catalog.CatalogViewModel
import ru.babaetskv.passionwoman.app.presentation.feature.category.CategoryFragment
import ru.babaetskv.passionwoman.app.presentation.feature.category.CategoryViewModel
import ru.babaetskv.passionwoman.app.presentation.feature.navigation.NavigationViewModel
import ru.babaetskv.passionwoman.app.presentation.feature.splash.SplashViewModel
import ru.babaetskv.passionwoman.app.utils.notifier.Notifier
import ru.babaetskv.passionwoman.data.api.ApiProvider
import ru.babaetskv.passionwoman.data.api.ApiProviderImpl
import ru.babaetskv.passionwoman.data.repository.CatalogRepositoryImpl
import ru.babaetskv.passionwoman.domain.interactor.GetCategoriesUseCase
import ru.babaetskv.passionwoman.domain.interactor.GetProductsUseCase
import ru.babaetskv.passionwoman.domain.repository.CatalogRepository

val appModule = module {
    single { Notifier(get()) }
}

val navigationModule = module {
    single { Cicerone.create() }
    single { get<Cicerone<Router>>().router }
    single { get<Cicerone<Router>>().getNavigatorHolder() }
}

val viewModelModule = module {
    viewModel { SplashViewModel(get()) }
    viewModel { CatalogViewModel(get(), get()) }
    viewModel { (args: CategoryFragment.Args) ->
        CategoryViewModel(args, get(), get(), get())
    }
    viewModel { NavigationViewModel(get()) }
}

val interactorModule = module {
    factory { GetCategoriesUseCase(get()) }
    factory { GetProductsUseCase(get()) }
}

val repositoryModule = module {
    single<CatalogRepository> { CatalogRepositoryImpl(get()) }
}

val networkModule = module {
    single<ApiProvider> { ApiProviderImpl(get()) }
    single { get<ApiProvider>().provideApi() }
}
