package ru.babaetskv.passionwoman.app.presentation.feature.splash

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import ru.babaetskv.passionwoman.app.R
import ru.babaetskv.passionwoman.app.analytics.constants.ScreenKeys
import ru.babaetskv.passionwoman.app.navigation.Screens
import ru.babaetskv.passionwoman.app.presentation.base.BaseFragment
import ru.babaetskv.passionwoman.app.utils.deeplink.DeeplinkPayload

@Deprecated("Use Android 12 SplashScreen API")
class SplashFragment :
    BaseFragment<SplashViewModel, SplashViewModel.Router, SplashFragment.Args>() {
    override val viewModel: SplashViewModel by viewModel<SplashViewModelImpl> {
        parametersOf(args)
    }
    override val layoutRes: Int = R.layout.fragment_splash
    override val applyTopInset: Boolean = false
    override val applyBottomInset: Boolean = false
    override val screenName: String = ScreenKeys.SPLASH

    override fun handleRouterEvent(event: SplashViewModel.Router) {
        super.handleRouterEvent(event)
        when (event) {
            SplashViewModel.Router.OnboardingScreen -> router.newRootScreen(Screens.onboarding())
            SplashViewModel.Router.AuthScreen -> router.newRootScreen(Screens.auth(true))
            is SplashViewModel.Router.SignUpScreen -> {
                router.newRootScreen(Screens.signUp(event.profile, true))
            }
            is SplashViewModel.Router.NavigationScreen -> {
                router.newRootScreen(Screens.navigation(event.payload))
            }
        }
    }

    @Parcelize
    data class Args(
        val payload: DeeplinkPayload?
    ) : Parcelable

    companion object {

        fun create(payload: DeeplinkPayload?) = SplashFragment().withArgs(Args(payload))
    }
}
