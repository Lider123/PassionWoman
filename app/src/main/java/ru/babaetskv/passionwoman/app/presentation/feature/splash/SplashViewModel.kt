package ru.babaetskv.passionwoman.app.presentation.feature.splash

import ru.babaetskv.passionwoman.app.presentation.base.IViewModel
import ru.babaetskv.passionwoman.app.presentation.event.RouterEvent
import ru.babaetskv.passionwoman.app.utils.deeplink.DeeplinkPayload
import ru.babaetskv.passionwoman.domain.model.Profile

@Deprecated("Use Android 12 SplashScreen API")
interface SplashViewModel : IViewModel {

    sealed class Router : RouterEvent {

        object OnboardingScreen : Router()

        object AuthScreen : Router()

        data class SignUpScreen(
            val profile: Profile
        ) : Router()

        data class NavigationScreen(
            val payload: DeeplinkPayload?
        ) : Router()
    }
}
