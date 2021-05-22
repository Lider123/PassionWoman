package ru.babaetskv.passionwoman.app.presentation.feature.splash

import com.github.terrakok.cicerone.Router
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.babaetskv.passionwoman.app.Screens
import ru.babaetskv.passionwoman.app.presentation.base.BaseViewModel
import ru.babaetskv.passionwoman.app.utils.notifier.Notifier
import ru.babaetskv.passionwoman.data.preferences.Preferences

class SplashViewModel(
    private val preferences: Preferences,
    notifier: Notifier,
    router: Router
) : BaseViewModel(notifier, router) {

    override fun onResume() {
        super.onResume()
        launch {
            delay(DELAY)
            val nextScreen = if (preferences.onboardingShowed) {
                Screens.navigation()
            } else Screens.onboarding()
            router.replaceScreen(nextScreen)
        }
    }

    companion object {
        private const val DELAY = 2300L
    }
}
