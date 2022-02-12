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
import ru.babaetskv.passionwoman.app.presentation.event.EventHub
import ru.babaetskv.passionwoman.app.presentation.feature.contacts.ContactsViewModelImpl
import ru.babaetskv.passionwoman.app.presentation.feature.auth.AuthViewModelImpl
import ru.babaetskv.passionwoman.app.presentation.feature.auth.signup.EditProfileFragment
import ru.babaetskv.passionwoman.app.presentation.feature.auth.signup.EditProfileViewModelImpl
import ru.babaetskv.passionwoman.app.presentation.feature.catalog.CatalogViewModelImpl
import ru.babaetskv.passionwoman.app.presentation.feature.home.HomeViewModelImpl
import ru.babaetskv.passionwoman.app.presentation.feature.home.stories.StoriesFragment
import ru.babaetskv.passionwoman.app.presentation.feature.home.stories.StoriesViewModelImpl
import ru.babaetskv.passionwoman.app.presentation.feature.navigation.NavigationFragment
import ru.babaetskv.passionwoman.app.presentation.feature.navigation.NavigationViewModelImpl
import ru.babaetskv.passionwoman.app.presentation.feature.onboarding.OnboardingViewModelImpl
import ru.babaetskv.passionwoman.app.presentation.feature.productcard.ProductCardFragment
import ru.babaetskv.passionwoman.app.presentation.feature.productcard.ProductCardViewModelImpl
import ru.babaetskv.passionwoman.app.presentation.feature.productlist.FavoritesViewModelImpl
import ru.babaetskv.passionwoman.app.presentation.feature.productlist.ProductListFragment
import ru.babaetskv.passionwoman.app.presentation.feature.productlist.ProductListViewModelImpl
import ru.babaetskv.passionwoman.app.presentation.feature.productlist.filters.FiltersFragment
import ru.babaetskv.passionwoman.app.presentation.feature.productlist.filters.FiltersViewModelImpl
import ru.babaetskv.passionwoman.data.datasource.ProductsPagingSourceFactory
import ru.babaetskv.passionwoman.app.presentation.feature.productlist.sorting.SortingFragment
import ru.babaetskv.passionwoman.app.presentation.feature.productlist.sorting.SortingViewModelImpl
import ru.babaetskv.passionwoman.app.presentation.feature.profile.ProfileViewModelImpl
import ru.babaetskv.passionwoman.app.presentation.feature.splash.SplashFragment
import ru.babaetskv.passionwoman.app.presentation.feature.splash.SplashViewModelImpl
import ru.babaetskv.passionwoman.app.utils.ExternalIntentHandler
import ru.babaetskv.passionwoman.app.utils.NetworkStateChecker
import ru.babaetskv.passionwoman.app.utils.deeplink.DeeplinkGenerator
import ru.babaetskv.passionwoman.app.utils.deeplink.DeeplinkHandler
import ru.babaetskv.passionwoman.app.utils.deeplink.FirebaseDeeplinkGenerator
import ru.babaetskv.passionwoman.app.utils.deeplink.FirebaseDeeplinkHandler
import ru.babaetskv.passionwoman.app.utils.notifier.Notifier
import ru.babaetskv.passionwoman.data.api.ApiProvider
import ru.babaetskv.passionwoman.data.api.ApiProviderImpl
import ru.babaetskv.passionwoman.data.gateway.AuthGatewayImpl
import ru.babaetskv.passionwoman.data.gateway.CatalogGatewayImpl
import ru.babaetskv.passionwoman.data.preferences.PreferencesProvider
import ru.babaetskv.passionwoman.data.preferences.PreferencesProviderImpl
import ru.babaetskv.passionwoman.app.presentation.interactor.*
import ru.babaetskv.passionwoman.domain.StringProvider
import ru.babaetskv.passionwoman.domain.gateway.*
import ru.babaetskv.passionwoman.domain.model.Sorting
import ru.babaetskv.passionwoman.domain.model.filters.Filter
import ru.babaetskv.passionwoman.domain.usecase.*

val appModule = module {
    single<Resources> { androidContext().resources }
    single { Notifier(get()) }
    single<StringProvider> { StringProviderImpl(get()) }
    single<AuthHandler> { AuthHandlerImpl(get()) }
    single { EventHub() }
    single { ExternalIntentHandler(androidContext()) }
    single<AnalyticsHandler> { FirebaseAnalyticsHandler(get()) }
    single<ErrorLogger> { FirebaseErrorLogger(get()) }
    single { NetworkStateChecker(androidContext()) }
    single<DeeplinkGenerator> { FirebaseDeeplinkGenerator() }
    single<DeeplinkHandler> { FirebaseDeeplinkHandler() }
}

val navigationModule = module {
    single { Cicerone.create(AppRouter()) }
    single { get<Cicerone<AppRouter>>().router }
    single { get<Cicerone<AppRouter>>().getNavigatorHolder() }
}

val viewModelModule = module {
    single { ViewModelDependencies(get(), get(), get(), get(), get()) }
    viewModel { MainViewModel(get(), get()) }
    viewModel { (args: SplashFragment.Args) ->
        SplashViewModelImpl(args, get(), get(), get(), get())
    }
    viewModel { CatalogViewModelImpl(get(), get()) }
    viewModel { (args: ProductListFragment.Args) ->
        ProductListViewModelImpl(args,
            stringProvider = get(),
            productsPagingSourceFactory = get { parametersOf(args.categoryId, args.filters, args.sorting) },
            dependencies = get()
        )
    }
    viewModel { (args: NavigationFragment.Args) ->
        NavigationViewModelImpl(args, get(), get(), get())
    }
    viewModel { OnboardingViewModelImpl(get(), get()) }
    viewModel { AuthViewModelImpl(get(), get(), get(), get()) }
    viewModel { (args: EditProfileFragment.Args) ->
        EditProfileViewModelImpl(args, get(), get())
    }
    viewModel { ProfileViewModelImpl(get(), get(), get(), get(), get(), get()) }
    viewModel { (args: ProductCardFragment.Args) ->
        ProductCardViewModelImpl(args, get(), get(), get(), get(), get(), get(), get())
    }
    viewModel { HomeViewModelImpl(get(), get(), get()) }
    viewModel { (args: SortingFragment.Args) ->
        SortingViewModelImpl(args, get(), get())
    }
    viewModel { FavoritesViewModelImpl(get(), get(), get(), get(), get()) }
    viewModel { ContactsViewModelImpl(get(), get()) }
    viewModel { (args: FiltersFragment.Args) ->
        FiltersViewModelImpl(args, get(), get())
    }
    viewModel { (args: StoriesFragment.Args) ->
        StoriesViewModelImpl(args, get())
    }
}

val dataSourceModule = module {
    factory { (categoryId: String, filters: List<Filter>, sorting: Sorting) ->
        ProductsPagingSourceFactory(get(), get(),
            categoryId = categoryId,
            filters = filters,
            sorting = sorting
        )
    }
}

val interactorModule = module {
    factory<GetCategoriesUseCase> { GetCategoriesInteractor(get(), get()) }
    factory<AuthorizeAsGuestUseCase> { AuthorizeAsGuestInteractor(get(), get()) }
    factory<AuthorizeUseCase> { AuthorizeInteractor(get(), get(), get()) }
    factory<GetProfileUseCase> { GetProfileInteractor(get(), get()) }
    factory<UpdateProfileUseCase> { UpdateProfileInteractor(get(), get(), get()) }
    factory<LogOutUseCase> { LogOutInteractor(get(), get(), get()) }
    factory<UpdateAvatarUseCase> { UpdateAvatarInteractor(get(), get()) }
    factory<GetHomeDataUseCase> { GetHomeDataInteractor(get(), get()) }
    factory<GetFavoritesUseCase> { GetFavoritesInteractor(get(), get(), get()) }
    factory<GetProductUseCase> { GetProductInteractor(get(), get()) }
    factory<AddToFavoritesUseCase> { AddToFavoritesInteractor(get(), get()) }
    factory<RemoveFromFavoritesUseCase> { RemoveFromFavoritesInteractor(get(), get()) }
    factory<SyncFavoritesUseCase> { SyncFavoritesInteractor(get(), get(), get()) }
    factory<GetProductsUseCase> { GetProductsInteractor(get(), get()) }
}

val gatewayModule = module {
    single<CatalogGateway> { CatalogGatewayImpl(get()) }
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
