package ru.babaetskv.passionwoman.app.di

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import androidx.work.WorkManager
import androidx.work.WorkerFactory
import com.github.terrakok.cicerone.Cicerone
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.babaetskv.passionwoman.app.R
import ru.babaetskv.passionwoman.app.analytics.base.AnalyticsHandler
import ru.babaetskv.passionwoman.app.analytics.FirebaseAnalyticsHandler
import ru.babaetskv.passionwoman.app.analytics.FirebaseErrorLogger
import ru.babaetskv.passionwoman.app.analytics.base.ErrorLogger
import ru.babaetskv.passionwoman.app.auth.AuthHandler
import ru.babaetskv.passionwoman.app.auth.AuthHandlerImpl
import ru.babaetskv.passionwoman.app.utils.StringProviderImpl
import ru.babaetskv.passionwoman.app.navigation.AppRouter
import ru.babaetskv.passionwoman.app.presentation.MainViewModelImpl
import ru.babaetskv.passionwoman.app.AppConfig
import ru.babaetskv.passionwoman.app.permission.PermissionManager
import ru.babaetskv.passionwoman.app.presentation.base.ViewModelDependencies
import ru.babaetskv.passionwoman.app.presentation.event.EventHub
import ru.babaetskv.passionwoman.app.presentation.feature.auth.AuthFragment
import ru.babaetskv.passionwoman.app.presentation.feature.contacts.ContactsViewModelImpl
import ru.babaetskv.passionwoman.app.presentation.feature.auth.AuthViewModelImpl
import ru.babaetskv.passionwoman.app.presentation.feature.auth.signup.EditProfileFragment
import ru.babaetskv.passionwoman.app.presentation.feature.auth.signup.EditProfileViewModelImpl
import ru.babaetskv.passionwoman.app.presentation.feature.cart.CartViewModelImpl
import ru.babaetskv.passionwoman.app.presentation.feature.cart.newcartitem.AddToCartFragment
import ru.babaetskv.passionwoman.app.presentation.feature.cart.newcartitem.AddToCartViewModelImpl
import ru.babaetskv.passionwoman.app.presentation.feature.catalog.CatalogViewModelImpl
import ru.babaetskv.passionwoman.app.presentation.feature.home.HomeViewModelImpl
import ru.babaetskv.passionwoman.app.presentation.feature.home.stories.StoriesFragment
import ru.babaetskv.passionwoman.app.presentation.feature.home.stories.StoriesViewModelImpl
import ru.babaetskv.passionwoman.app.presentation.feature.navigation.NavigationFragment
import ru.babaetskv.passionwoman.app.presentation.feature.navigation.NavigationViewModelImpl
import ru.babaetskv.passionwoman.app.presentation.feature.onboarding.OnboardingViewModelImpl
import ru.babaetskv.passionwoman.app.presentation.feature.orderlist.OrderListViewModelImpl
import ru.babaetskv.passionwoman.app.presentation.feature.productcard.ProductCardFragment
import ru.babaetskv.passionwoman.app.presentation.feature.productcard.ProductCardViewModelImpl
import ru.babaetskv.passionwoman.app.presentation.feature.productlist.FavoritesViewModelImpl
import ru.babaetskv.passionwoman.app.presentation.feature.productlist.ProductListFragment
import ru.babaetskv.passionwoman.app.presentation.feature.productlist.ProductListViewModelImpl
import ru.babaetskv.passionwoman.app.presentation.feature.productlist.filters.FiltersFragment
import ru.babaetskv.passionwoman.app.presentation.feature.productlist.filters.FiltersViewModelImpl
import ru.babaetskv.passionwoman.app.presentation.feature.productlist.sorting.SortingFragment
import ru.babaetskv.passionwoman.app.presentation.feature.productlist.sorting.SortingViewModelImpl
import ru.babaetskv.passionwoman.app.presentation.feature.profile.ProfileViewModelImpl
import ru.babaetskv.passionwoman.app.utils.externalaction.ExternalIntentHandler
import ru.babaetskv.passionwoman.app.utils.NetworkStateChecker
import ru.babaetskv.passionwoman.app.utils.deeplink.DeeplinkGenerator
import ru.babaetskv.passionwoman.app.utils.deeplink.DeeplinkHandler
import ru.babaetskv.passionwoman.app.utils.deeplink.FirebaseDeeplinkGenerator
import ru.babaetskv.passionwoman.app.utils.deeplink.FirebaseDeeplinkHandler
import ru.babaetskv.passionwoman.app.utils.notifier.NotifierImpl
import ru.babaetskv.passionwoman.data.api.ApiProvider
import ru.babaetskv.passionwoman.data.api.ApiProviderImpl
import ru.babaetskv.passionwoman.data.preferences.PreferencesProvider
import ru.babaetskv.passionwoman.data.preferences.PreferencesProviderImpl
import ru.babaetskv.passionwoman.app.presentation.interactor.*
import ru.babaetskv.passionwoman.app.presentation.worker.RegisterPushTokenWorker
import ru.babaetskv.passionwoman.app.presentation.worker.UnregisterPushTokenWorker
import ru.babaetskv.passionwoman.app.presentation.worker.base.SampleWorkerFactory
import ru.babaetskv.passionwoman.app.push.AppNotificationDataConverter
import ru.babaetskv.passionwoman.app.push.AppNotificationDataConverterImpl
import ru.babaetskv.passionwoman.app.push.AppNotificationManager
import ru.babaetskv.passionwoman.app.utils.bool
import ru.babaetskv.passionwoman.app.utils.datetime.DefaultDateTimeConverter
import ru.babaetskv.passionwoman.app.utils.externalaction.ExternalActionHandler
import ru.babaetskv.passionwoman.app.utils.notifier.Notifier
import ru.babaetskv.passionwoman.data.assets.AssetProvider
import ru.babaetskv.passionwoman.data.assets.AssetProviderImpl
import ru.babaetskv.passionwoman.data.dataflow.CartFlowImpl
import ru.babaetskv.passionwoman.domain.gateway.exception.GatewayExceptionProvider
import ru.babaetskv.passionwoman.data.gateway.exception.GatewayExceptionProviderImpl
import ru.babaetskv.passionwoman.data.gateway.provider.GatewayProvider
import ru.babaetskv.passionwoman.data.gateway.provider.GatewayProviderImpl
import ru.babaetskv.passionwoman.domain.StringProvider
import ru.babaetskv.passionwoman.domain.dataflow.CartFlow
import ru.babaetskv.passionwoman.domain.usecase.*

val appModule = module {
    single<Resources> { androidContext().resources }
    single<Notifier> { NotifierImpl(get()) }
    single<StringProvider> { StringProviderImpl(get()) }
    single<AuthHandler> { AuthHandlerImpl(get()) }
    single { EventHub() }
    single<ExternalActionHandler> { ExternalIntentHandler(androidContext()) }
    single<AnalyticsHandler> { FirebaseAnalyticsHandler(get()) }
    single<ErrorLogger> { FirebaseErrorLogger(get()) }
    single { NetworkStateChecker(androidContext()) }
    single<DeeplinkGenerator> { FirebaseDeeplinkGenerator() }
    single<DeeplinkHandler> { FirebaseDeeplinkHandler() }
    single<AppNotificationDataConverter> { AppNotificationDataConverterImpl() }
    single { WorkManager.getInstance(androidContext()) }
    single<WorkerFactory> {
        SampleWorkerFactory(mapOf(
            RegisterPushTokenWorker::class.java to RegisterPushTokenWorker.Factory(get(), get()),
            UnregisterPushTokenWorker::class.java to UnregisterPushTokenWorker.Factory(get(), get())
        ))
    }
    single { AppNotificationManager(androidContext()) }
}

val navigationModule = module {
    single { Cicerone.create(AppRouter()) }
    single { get<Cicerone<AppRouter>>().router }
    single { get<Cicerone<AppRouter>>().getNavigatorHolder() }
}

val viewModelModule = module {
    single {
        val isPortraitModeOnly = get<Context>().bool(R.bool.portrait_mode_only)
        AppConfig(isPortraitModeOnly)
    }
    single { ViewModelDependencies(get(), get(), get(), get(), get(), get(), get()) }
    viewModel { MainViewModelImpl(get(), get(), get(), get(), get(), get(), get()) }
    viewModel { CatalogViewModelImpl(get(), get()) }
    viewModel { (args: ProductListFragment.Args) ->
        ProductListViewModelImpl(args, get(), get(), get())
    }
    viewModel { (args: NavigationFragment.Args) ->
        NavigationViewModelImpl(args, get(), get(), get(), get(), get())
    }
    viewModel { OnboardingViewModelImpl(get(), get()) }
    viewModel { (args: AuthFragment.Args) ->
        AuthViewModelImpl(args, get(), get(), get(), get(), get())
    }
    viewModel { (args: EditProfileFragment.Args) ->
        EditProfileViewModelImpl(args, get(), get())
    }
    viewModel { ProfileViewModelImpl(get(), get(), get(), get(), get(), get(), get()) }
    viewModel { (args: ProductCardFragment.Args) ->
        ProductCardViewModelImpl(args, get(), get(), get(), get(), get(), get(), get(), get())
    }
    viewModel { (activity: Activity) ->
        val permissionManager = PermissionManager(activity)
        HomeViewModelImpl(permissionManager, get(), get(), get())
    }
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
    viewModel {
        CartViewModelImpl(get(), get(), get(), get(), get(), get())
    }
    viewModel { (args: AddToCartFragment.Args) ->
        AddToCartViewModelImpl(args, get(), get())
    }
    viewModel {
        OrderListViewModelImpl(get(), get(), get())
    }
}

val interactorModule = module {
    factory<GetCategoriesUseCase> { GetCategoriesInteractor(get(), get()) }
    factory<AuthorizeAsGuestUseCase> { AuthorizeAsGuestInteractor(get(), get()) }
    factory<AuthorizeUseCase> { AuthorizeInteractor(get(), get(), get(), get()) }
    factory<GetProfileUseCase> { GetProfileInteractor(get(), get()) }
    factory<UpdateProfileUseCase> { UpdateProfileInteractor(get(), get(), get()) }
    factory<LogOutUseCase> { LogOutInteractor(get(), get(), get(), get()) }
    factory<UpdateAvatarUseCase> { UpdateAvatarInteractor(get(), get()) }
    factory<GetHomeDataUseCase> { GetHomeDataInteractor(get(), get(), get()) }
    factory<GetFavoritesUseCase> { GetFavoritesInteractor(get(), get(), get()) }
    factory<GetProductUseCase> { GetProductInteractor(get(), get()) }
    factory<AddToFavoritesUseCase> { AddToFavoritesInteractor(get(), get()) }
    factory<RemoveFromFavoritesUseCase> { RemoveFromFavoritesInteractor(get(), get()) }
    factory<SyncFavoritesUseCase> { SyncFavoritesInteractor(get(), get(), get()) }
    factory<GetProductsUseCase> { GetProductsInteractor(get(), get()) }
    factory<AddToCartUseCase> { AddToCartInteractor(get(), get(), get()) }
    factory<RemoveFromCartUseCase> { RemoveFromCartInteractor(get(), get(), get()) }
    factory<SyncCartUseCase> { SyncCartInteractor(get(), get(), get()) }
    factory<GetOrdersUseCase> { GetOrdersInteractor(get(), get()) }
    factory<CheckoutUseCase> { CheckoutInteractor(get(), get(), get()) }
    factory<RegisterPushTokenUseCase> { RegisterPushTokenInteractor(get(), get()) }
    factory<UnregisterPushTokenUseCase> { UnregisterPushTokenInteractor(get(), get()) }
}

val gatewayModule = module {
    single<GatewayExceptionProvider> { GatewayExceptionProviderImpl(get()) }
    single<GatewayProvider> { GatewayProviderImpl(get(), get(), get()) }
    factory { get<GatewayProvider>().provideCatalogGateway() }
    factory { get<GatewayProvider>().provideAuthGateway() }
    factory { get<GatewayProvider>().provideProfileGateway() }
    factory { get<GatewayProvider>().provideCartGateway() }
    factory { get<GatewayProvider>().providePushGateway() }
}

val dataFlowModule = module {
    single<CartFlow> { CartFlowImpl(get()) }
}

val assetsModule = module {
    single<AssetProvider> { AssetProviderImpl(androidContext(), get()) }
}

val networkModule = module {
    single {
        Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }
    single<ApiProvider> { ApiProviderImpl(get(), get(), get(), get(), DefaultDateTimeConverter) }
    single { get<ApiProvider>().provideAuthApi() }
    single { get<ApiProvider>().provideCommonApi() }
}

val preferencesModule = module {
    single<PreferencesProvider> { PreferencesProviderImpl(get()) }
    single { get<PreferencesProvider>().provideAppPreferences() }
    single { get<PreferencesProvider>().provideAuthPreferences() }
    single { get<PreferencesProvider>().provideFavoritesPreferences() }
}
