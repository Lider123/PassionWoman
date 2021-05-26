package ru.babaetskv.passionwoman.app.presentation.feature.splash

import com.github.terrakok.cicerone.Router
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.babaetskv.passionwoman.app.Screens
import ru.babaetskv.passionwoman.app.presentation.base.BaseViewModel
import ru.babaetskv.passionwoman.app.utils.notifier.Notifier
import ru.babaetskv.passionwoman.domain.GetProfileUseCase
import ru.babaetskv.passionwoman.domain.preferences.AppPreferences
import ru.babaetskv.passionwoman.domain.preferences.AuthPreferences
import ru.babaetskv.passionwoman.domain.utils.execute

class SplashViewModel(
    private val appPreferences: AppPreferences,
    private val authPreferences: AuthPreferences,
    private val getProfileUseCase: GetProfileUseCase,
    notifier: Notifier,
    router: Router
) : BaseViewModel(notifier, router) {

    override fun onResume() {
        super.onResume()
        launch {
            delay(DELAY)
            when {
                !appPreferences.onboardingShowed -> router.newRootScreen(Screens.onboarding())
                authPreferences.authType == AuthPreferences.AuthType.NONE -> {
                    router.newRootScreen(Screens.auth())
                }
                authPreferences.authType == AuthPreferences.AuthType.AUTHORIZED
                    && !authPreferences.profileIsFilled -> launchWithLoading {
                    val profile = getProfileUseCase.execute()
                    router.newRootScreen(Screens.signUp(profile))
                }
                else -> router.newRootScreen(Screens.navigation())
            }
        }
    }

    companion object {
        private const val DELAY = 2300L
    }
}
