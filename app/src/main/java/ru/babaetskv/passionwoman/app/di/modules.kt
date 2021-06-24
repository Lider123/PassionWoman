package ru.babaetskv.passionwoman.app.di

import android.content.res.Resources
import com.github.terrakok.cicerone.Cicerone
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.babaetskv.passionwoman.app.auth.AuthHandler
import ru.babaetskv.passionwoman.app.auth.AuthHandlerImpl
import ru.babaetskv.passionwoman.app.StringProviderImpl
import ru.babaetskv.passionwoman.app.navigation.AppRouter
import ru.babaetskv.passionwoman.app.presentation.MainViewModel
import ru.babaetskv.passionwoman.app.presentation.feature.auth.AuthViewModel
import ru.babaetskv.passionwoman.app.presentation.feature.auth.signup.EditProfileFragment
import ru.babaetskv.passionwoman.app.presentation.feature.auth.signup.EditProfileViewModel
import ru.babaetskv.passionwoman.app.presentation.feature.catalog.CatalogViewModel
import ru.babaetskv.passionwoman.app.presentation.feature.home.HomeViewModel
import ru.babaetskv.passionwoman.app.presentation.feature.navigation.NavigationViewModel
import ru.babaetskv.passionwoman.app.presentation.feature.onboarding.OnboardingViewModel
import ru.babaetskv.passionwoman.app.presentation.feature.productcard.ProductCardFragment
import ru.babaetskv.passionwoman.app.presentation.feature.productcard.ProductCardViewModel
import ru.babaetskv.passionwoman.app.presentation.feature.productlist.ProductListFragment
import ru.babaetskv.passionwoman.app.presentation.feature.productlist.ProductListViewModel
import ru.babaetskv.passionwoman.app.presentation.feature.productlist.sorting.SortingFragment
import ru.babaetskv.passionwoman.app.presentation.feature.productlist.sorting.SortingUpdateHub
import ru.babaetskv.passionwoman.app.presentation.feature.productlist.sorting.SortingViewModel
import ru.babaetskv.passionwoman.app.presentation.feature.profile.ProfileUpdatesListener
import ru.babaetskv.passionwoman.app.presentation.feature.profile.ProfileViewModel
import ru.babaetskv.passionwoman.app.presentation.feature.splash.SplashViewModel
import ru.babaetskv.passionwoman.app.utils.notifier.Notifier
import ru.babaetskv.passionwoman.data.api.ApiProvider
import ru.babaetskv.passionwoman.data.api.ApiProviderImpl
import ru.babaetskv.passionwoman.data.gateway.AuthGatewayImpl
import ru.babaetskv.passionwoman.data.preferences.AppPreferencesImpl
import ru.babaetskv.passionwoman.data.preferences.AuthPreferencesImpl
import ru.babaetskv.passionwoman.data.gateway.CatalogGatewayImpl
import ru.babaetskv.passionwoman.domain.interactor.*
import ru.babaetskv.passionwoman.domain.interactor.exception.StringProvider
import ru.babaetskv.passionwoman.domain.preferences.AppPreferences
import ru.babaetskv.passionwoman.domain.preferences.AuthPreferences
import ru.babaetskv.passionwoman.domain.gateway.*

val appModule = module {
    single<Resources> { androidContext().resources }
    single { Notifier(get()) }
    single<StringProvider> { StringProviderImpl(get()) }
    single<AuthHandler> { AuthHandlerImpl(get()) }
    single { SortingUpdateHub() }
}

val navigationModule = module {
    single { Cicerone.create(AppRouter()) }
    single { get<Cicerone<AppRouter>>().router }
    single { get<Cicerone<AppRouter>>().getNavigatorHolder() }
}

val viewModelModule = module {
    viewModel { MainViewModel(get()) }
    viewModel { SplashViewModel(get(), get(), get(), get()) }
    viewModel { CatalogViewModel(get(), get()) }
    viewModel { (args: ProductListFragment.Args) ->
        ProductListViewModel(args, get(), get(), get(), get())
    }
    viewModel { NavigationViewModel(get(), get()) }
    viewModel { OnboardingViewModel(get(), get()) }
    viewModel { AuthViewModel(get(), get(), get(), get()) }
    viewModel { (args: EditProfileFragment.Args, profileUpdatesListener: ProfileUpdatesListener) ->
        EditProfileViewModel(args, profileUpdatesListener, get(), get())
    }
    viewModel { ProfileViewModel(get(), get(), get(), get(), get()) }
    viewModel { (args: ProductCardFragment.Args) ->
        ProductCardViewModel(args, get())
    }
    viewModel { HomeViewModel(get(), get()) }
    viewModel { (args: SortingFragment.Args) ->
        SortingViewModel(args, get(), get(), get())
    }
}

val interactorModule = module {
    factory { GetCategoriesUseCase(get(), get()) }
    factory { GetProductsUseCase(get(), get()) }
    factory { AuthorizeAsGuestUseCase(get(), get()) }
    factory { AuthorizeUseCase(get(), get(), get()) }
    factory { GetProfileUseCase(get(), get()) }
    factory { UpdateProfileUseCase(get(), get(), get()) }
    factory { LogOutUseCase(get(), get()) }
    factory { UpdateAvatarUseCase(get(), get()) }
    factory { GetHomeDataUseCase(get(), get()) }
}

val gatewayModule = module {
    single<CatalogGateway> { CatalogGatewayImpl(get(), get()) }
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
    single<AppPreferences> { AppPreferencesImpl() }
    single<AuthPreferences> { AuthPreferencesImpl() }
}
