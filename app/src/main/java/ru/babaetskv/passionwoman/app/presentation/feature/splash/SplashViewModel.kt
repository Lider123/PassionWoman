package ru.babaetskv.passionwoman.app.presentation.feature.splash

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.babaetskv.passionwoman.app.presentation.base.BaseViewModel
import ru.babaetskv.passionwoman.app.presentation.base.RouterEvent
import ru.babaetskv.passionwoman.app.utils.notifier.Notifier
import ru.babaetskv.passionwoman.domain.interactor.GetProfileUseCase
import ru.babaetskv.passionwoman.domain.model.Profile
import ru.babaetskv.passionwoman.domain.preferences.AppPreferences
import ru.babaetskv.passionwoman.domain.preferences.AuthPreferences
import ru.babaetskv.passionwoman.domain.utils.execute

class SplashViewModel(
    private val appPreferences: AppPreferences,
    private val authPreferences: AuthPreferences,
    private val getProfileUseCase: GetProfileUseCase,
    notifier: Notifier
) : BaseViewModel<SplashViewModel.Router>(notifier) {

    override fun onResume() {
        super.onResume()
        launch {
            delay(DELAY)
            when {
                !appPreferences.onboardingShowed -> navigateTo(Router.OnboardingScreen)
                authPreferences.authType == AuthPreferences.AuthType.NONE -> {
                    navigateTo(Router.AuthScreen)
                }
                authPreferences.authType == AuthPreferences.AuthType.AUTHORIZED
                    && !authPreferences.profileIsFilled -> launchWithLoading {
                    val profile = getProfileUseCase.execute()
                    navigateTo(Router.SignUpScreen(profile))
                }
                else -> navigateTo(Router.NavigationScreen)
            }
        }
    }

    sealed class Router : RouterEvent {

        object OnboardingScreen : Router()

        object AuthScreen : Router()

        data class SignUpScreen(
            val profile: Profile
        ) : Router()

        object NavigationScreen : Router()
    }

    companion object {
        private const val DELAY = 2300L
    }
}
