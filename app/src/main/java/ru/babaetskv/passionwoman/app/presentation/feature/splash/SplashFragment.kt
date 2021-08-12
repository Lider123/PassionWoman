package ru.babaetskv.passionwoman.app.presentation.feature.splash

import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.babaetskv.passionwoman.app.R
import ru.babaetskv.passionwoman.app.Screens
import ru.babaetskv.passionwoman.app.presentation.base.BaseFragment
import ru.babaetskv.passionwoman.app.presentation.base.FragmentComponent

class SplashFragment :
    BaseFragment<SplashViewModel, SplashViewModel.Router, FragmentComponent.NoArgs>() {
    override val viewModel: SplashViewModel by viewModel()
    override val layoutRes: Int = R.layout.fragment_splash
    override val applyTopInset: Boolean = false
    override val applyBottomInset: Boolean = false

    override fun handleRouterEvent(event: SplashViewModel.Router) {
        super.handleRouterEvent(event)
        when (event) {
            SplashViewModel.Router.OnboardingScreen -> router.newRootScreen(Screens.onboarding())
            SplashViewModel.Router.AuthScreen -> router.newRootScreen(Screens.auth())
            is SplashViewModel.Router.SignUpScreen -> {
                router.newRootScreen(Screens.signUp(event.profile))
            }
            SplashViewModel.Router.NavigationScreen -> router.newRootScreen(Screens.navigation())
        }
    }

    companion object {

        fun create() = SplashFragment()
    }
}
