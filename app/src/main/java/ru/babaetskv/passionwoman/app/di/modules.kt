package ru.babaetskv.passionwoman.app.di

import android.content.res.Resources
import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.Router
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.babaetskv.passionwoman.app.auth.AuthHandler
import ru.babaetskv.passionwoman.app.auth.AuthHandlerImpl
import ru.babaetskv.passionwoman.app.exception.ErrorMessageProviderImpl
import ru.babaetskv.passionwoman.app.presentation.feature.auth.AuthViewModel
import ru.babaetskv.passionwoman.app.presentation.feature.auth.signup.SignUpFragment
import ru.babaetskv.passionwoman.app.presentation.feature.auth.signup.SignUpViewModel
import ru.babaetskv.passionwoman.app.presentation.feature.catalog.CatalogViewModel
import ru.babaetskv.passionwoman.app.presentation.feature.category.CategoryFragment
import ru.babaetskv.passionwoman.app.presentation.feature.category.CategoryViewModel
import ru.babaetskv.passionwoman.app.presentation.feature.navigation.NavigationViewModel
import ru.babaetskv.passionwoman.app.presentation.feature.onboarding.OnboardingViewModel
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
import ru.babaetskv.passionwoman.domain.interactor.exception.ErrorMessageProvider
import ru.babaetskv.passionwoman.domain.preferences.AppPreferences
import ru.babaetskv.passionwoman.domain.preferences.AuthPreferences
import ru.babaetskv.passionwoman.domain.gateway.*

val appModule = module {
    single<Resources> { androidContext().resources }
    single { Notifier(get()) }
    single<ErrorMessageProvider> { ErrorMessageProviderImpl(get()) }
    single<AuthHandler> { AuthHandlerImpl(get()) }
}

val navigationModule = module {
    single { Cicerone.create() }
    single { get<Cicerone<Router>>().router }
    single { get<Cicerone<Router>>().getNavigatorHolder() }
}

val viewModelModule = module {
    viewModel { SplashViewModel(get(), get(), get(), get(), get()) }
    viewModel { CatalogViewModel(get(), get(), get()) }
    viewModel { (args: CategoryFragment.Args) ->
        CategoryViewModel(args, get(), get(), get())
    }
    viewModel { NavigationViewModel(get(), get(), get()) }
    viewModel { OnboardingViewModel(get(), get(), get()) }
    viewModel { AuthViewModel(get(), get(), get(), get(), get()) }
    viewModel { (args: SignUpFragment.Args) ->
        SignUpViewModel(args, get(), get(), get())
    }
    viewModel { ProfileViewModel(get(), get(), get(), get(), get()) }
}

val interactorModule = module {
    factory { GetCategoriesUseCase(get(), get()) }
    factory { GetProductsUseCase(get(), get()) }
    factory { AuthorizeAsGuestUseCase(get(), get()) }
    factory { AuthorizeUseCase(get(), get(), get()) }
    factory { GetProfileUseCase(get(), get()) }
    factory { UpdateProfileUseCase(get(), get(), get()) }
    factory { LogOutUseCase(get(), get()) }
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
    single<AppPreferences> { AppPreferencesImpl() }
    single<AuthPreferences> { AuthPreferencesImpl() }
}
