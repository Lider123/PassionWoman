package ru.babaetskv.passionwoman.app.di

import android.content.res.Resources
import com.github.terrakok.cicerone.Cicerone
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module
import ru.babaetskv.passionwoman.app.analytics.base.AnalyticsHandler
import ru.babaetskv.passionwoman.app.analytics.FirebaseAnalyticsHandler
import ru.babaetskv.passionwoman.app.analytics.FirebaseErrorLogger
import ru.babaetskv.passionwoman.app.analytics.base.ErrorLogger
import ru.babaetskv.passionwoman.app.auth.AuthHandler
import ru.babaetskv.passionwoman.app.auth.AuthHandlerImpl
import ru.babaetskv.passionwoman.app.utils.StringProviderImpl
import ru.babaetskv.passionwoman.app.navigation.AppRouter
import ru.babaetskv.passionwoman.app.presentation.MainViewModel
import ru.babaetskv.passionwoman.app.presentation.base.ViewModelDependencies
import ru.babaetskv.passionwoman.app.presentation.feature.contacts.ContactsViewModel
import ru.babaetskv.passionwoman.app.presentation.feature.auth.AuthViewModel
import ru.babaetskv.passionwoman.app.presentation.feature.auth.signup.EditProfileFragment
import ru.babaetskv.passionwoman.app.presentation.feature.auth.signup.EditProfileViewModel
import ru.babaetskv.passionwoman.app.presentation.feature.catalog.CatalogViewModel
import ru.babaetskv.passionwoman.app.presentation.feature.home.HomeViewModel
import ru.babaetskv.passionwoman.app.presentation.feature.navigation.NavigationViewModel
import ru.babaetskv.passionwoman.app.presentation.feature.onboarding.OnboardingViewModel
import ru.babaetskv.passionwoman.app.presentation.feature.productcard.ProductCardFragment
import ru.babaetskv.passionwoman.app.presentation.feature.productcard.ProductCardViewModel
import ru.babaetskv.passionwoman.app.presentation.feature.productlist.FavoritesViewModel
import ru.babaetskv.passionwoman.app.presentation.feature.productlist.ProductListFragment
import ru.babaetskv.passionwoman.app.presentation.feature.productlist.ProductListViewModel
import ru.babaetskv.passionwoman.app.presentation.feature.productlist.sorting.SortingFragment
import ru.babaetskv.passionwoman.app.presentation.feature.productlist.sorting.SortingUpdateHub
import ru.babaetskv.passionwoman.app.presentation.feature.productlist.sorting.SortingViewModel
import ru.babaetskv.passionwoman.app.presentation.feature.profile.ProfileUpdatesListener
import ru.babaetskv.passionwoman.app.presentation.feature.profile.ProfileViewModel
import ru.babaetskv.passionwoman.app.presentation.feature.splash.SplashViewModel
import ru.babaetskv.passionwoman.app.utils.ExternalIntentHandler
import ru.babaetskv.passionwoman.app.utils.NetworkStateChecker
import ru.babaetskv.passionwoman.app.utils.notifier.Notifier
import ru.babaetskv.passionwoman.data.api.ApiProvider
import ru.babaetskv.passionwoman.data.api.ApiProviderImpl
import ru.babaetskv.passionwoman.data.datasource.ProductsDataSource
import ru.babaetskv.passionwoman.data.gateway.AuthGatewayImpl
import ru.babaetskv.passionwoman.data.gateway.CatalogGatewayImpl
import ru.babaetskv.passionwoman.data.preferences.PreferencesProvider
import ru.babaetskv.passionwoman.data.preferences.PreferencesProviderImpl
import ru.babaetskv.passionwoman.domain.interactor.*
import ru.babaetskv.passionwoman.domain.interactor.exception.StringProvider
import ru.babaetskv.passionwoman.domain.gateway.*
import ru.babaetskv.passionwoman.domain.model.Filters
import ru.babaetskv.passionwoman.domain.model.Sorting

val appModule = module {
    single<Resources> { androidContext().resources }
    single { Notifier(get()) }
    single<StringProvider> { StringProviderImpl(get()) }
    single<AuthHandler> { AuthHandlerImpl(get()) }
    single { SortingUpdateHub() }
    single { ExternalIntentHandler(androidContext()) }
    single<AnalyticsHandler> { FirebaseAnalyticsHandler(get()) }
    single<ErrorLogger> { FirebaseErrorLogger(get()) }
    single { NetworkStateChecker(androidContext()) }
}

val navigationModule = module {
    single { Cicerone.create(AppRouter()) }
    single { get<Cicerone<AppRouter>>().router }
    single { get<Cicerone<AppRouter>>().getNavigatorHolder() }
}

val viewModelModule = module {
    single { ViewModelDependencies(get(), get(), get(), get()) }
    viewModel { MainViewModel(get()) }
    viewModel { SplashViewModel(get(), get(), get(), get()) }
    viewModel { CatalogViewModel(get(), get()) }
    viewModel { (args: ProductListFragment.Args) ->
        ProductListViewModel(args,
            sortingUpdateHub = get(),
            stringProvider = get(),
            productsDataSource = get { parametersOf(args.categoryId, args.filters, args.sorting) },
            dependencies = get()
        )
    }
    viewModel { NavigationViewModel(get(), get(), get()) }
    viewModel { OnboardingViewModel(get(), get()) }
    viewModel { AuthViewModel(get(), get(), get(), get()) }
    viewModel { (args: EditProfileFragment.Args, profileUpdatesListener: ProfileUpdatesListener) ->
        EditProfileViewModel(args, profileUpdatesListener, get(), get())
    }
    viewModel { ProfileViewModel(get(), get(), get(), get(), get()) }
    viewModel { (args: ProductCardFragment.Args) ->
        ProductCardViewModel(args, get(), get(), get(), get(), get())
    }
    viewModel { HomeViewModel(get(), get()) }
    viewModel { (args: SortingFragment.Args) ->
        SortingViewModel(args, get(), get(), get())
    }
    viewModel { FavoritesViewModel(get(), get(), get(), get(), get()) }
    viewModel { ContactsViewModel(get(), get()) }
}

val dataSourceModule = module {
    factory { (categoryId: String, filters: Filters, sorting: Sorting) ->
        ProductsDataSource(get(), get(),
            categoryId = categoryId,
            filters = filters,
            sorting = sorting
        )
    }
}

val interactorModule = module {
    factory { GetCategoriesUseCase(get(), get()) }
    factory { AuthorizeAsGuestUseCase(get(), get()) }
    factory { AuthorizeUseCase(get(), get(), get()) }
    factory { GetProfileUseCase(get(), get()) }
    factory { UpdateProfileUseCase(get(), get(), get()) }
    factory { LogOutUseCase(get(), get(), get()) }
    factory { UpdateAvatarUseCase(get(), get()) }
    factory { GetHomeDataUseCase(get(), get()) }
    factory { GetFavoritesUseCase(get(), get()) }
    factory { GetProductUseCase(get(), get()) }
    factory { AddToFavoritesUseCase(get(), get()) }
    factory { RemoveFromFavoritesUseCase(get(), get()) }
    factory { SyncFavoritesUseCase(get(), get(), get()) }
}

val gatewayModule = module {
    single<CatalogGateway> { CatalogGatewayImpl(get(), get(), get()) }
    single<AuthGateway> { AuthGatewayImpl(get(), get()) }
}

val networkModule = module {
    single {
        Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }
    single<ApiProvider> { ApiProviderImpl(get(), get(), get()) }
    single { get<ApiProvider>().provideAuthApi() }
    single { get<ApiProvider>().provideCommonApi() }
}

val preferencesModule = module {
    single<PreferencesProvider> { PreferencesProviderImpl(get()) }
    single { get<PreferencesProvider>().provideAppPreferences() }
    single { get<PreferencesProvider>().provideAuthPreferences() }
    single { get<PreferencesProvider>().provideFavoritesPreferences() }
}
