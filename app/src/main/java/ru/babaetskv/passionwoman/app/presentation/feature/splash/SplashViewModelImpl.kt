package ru.babaetskv.passionwoman.app.presentation.feature.splash

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.babaetskv.passionwoman.app.presentation.base.BaseViewModel
import ru.babaetskv.passionwoman.app.presentation.base.ViewModelDependencies
import ru.babaetskv.passionwoman.domain.preferences.AppPreferences
import ru.babaetskv.passionwoman.domain.preferences.AuthPreferences
import ru.babaetskv.passionwoman.domain.usecase.GetProfileUseCase

class SplashViewModelImpl(
    private val args: SplashFragment.Args,
    private val appPreferences: AppPreferences,
    private val authPreferences: AuthPreferences,
    private val getProfileUseCase: GetProfileUseCase,
    dependencies: ViewModelDependencies
) : BaseViewModel<SplashViewModel.Router>(dependencies), SplashViewModel {

    override fun onResume() {
        super.onResume()
        launch {
            delay(DELAY)
            when {
                !appPreferences.onboardingShowed -> {
                    navigateTo(SplashViewModel.Router.OnboardingScreen)
                }
                authPreferences.authType == AuthPreferences.AuthType.NONE -> {
                    navigateTo(SplashViewModel.Router.AuthScreen)
                }
                authPreferences.authType == AuthPreferences.AuthType.AUTHORIZED
                    && !authPreferences.profileIsFilled -> launchWithLoading {
                    val profile = getProfileUseCase.execute()
                    navigateTo(SplashViewModel.Router.SignUpScreen(profile))
                }
                else -> navigateTo(SplashViewModel.Router.NavigationScreen(args.payload))
            }
        }
    }

    companion object {
        private const val DELAY = 2300L
    }
}
