package ru.babaetskv.passionwoman.app.presentation.feature.splash

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.babaetskv.passionwoman.app.navigation.ScreenProvider
import ru.babaetskv.passionwoman.app.presentation.base.BaseViewModel
import ru.babaetskv.passionwoman.app.presentation.base.ViewModelDependencies
import ru.babaetskv.passionwoman.domain.preferences.AppPreferences
import ru.babaetskv.passionwoman.domain.preferences.AuthPreferences
import ru.babaetskv.passionwoman.domain.usecase.GetProfileUseCase
import ru.babaetskv.passionwoman.domain.usecase.base.UseCase.Companion.execute

@Deprecated("Use Android 12 SplashScreen API")
class SplashViewModelImpl(
    private val args: SplashFragment.Args,
    private val appPreferences: AppPreferences,
    private val authPreferences: AuthPreferences,
    private val getProfileUseCase: GetProfileUseCase,
    dependencies: ViewModelDependencies
) : BaseViewModel(dependencies), SplashViewModel {

    override fun onResume() {
        super.onResume()
        launch {
            delay(DELAY)
            when {
                !appPreferences.onboardingShowed -> router.newRootScreen(ScreenProvider.onboarding())
                authPreferences.authType == AuthPreferences.AuthType.NONE -> {
                    router.newRootScreen(ScreenProvider.auth(true))
                }
                authPreferences.authType == AuthPreferences.AuthType.AUTHORIZED
                    && !authPreferences.profileIsFilled -> launchWithLoading {
                    val profile = getProfileUseCase.execute()
                    router.newRootScreen(ScreenProvider.signUp(profile, true))
                }
                else -> router.newRootScreen(ScreenProvider.navigation(args.payload))
            }
        }
    }

    companion object {
        private const val DELAY = 2300L
    }
}
