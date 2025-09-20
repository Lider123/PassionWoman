package ru.babaetskv.passionwoman.app.presentation.feature.splash

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import ru.babaetskv.passionwoman.app.R
import ru.babaetskv.passionwoman.app.analytics.constants.ScreenKeys
import ru.babaetskv.passionwoman.app.presentation.base.BaseFragment
import ru.babaetskv.passionwoman.app.utils.deeplink.DeeplinkPayload

@Deprecated("Use Android 12 SplashScreen API")
class SplashFragment : BaseFragment<SplashViewModel, SplashFragment.Args>() {
    override val viewModel: SplashViewModel by viewModel<SplashViewModelImpl> {
        parametersOf(args)
    }
    override val layoutRes: Int = R.layout.fragment_splash
    override val applyTopInset: Boolean = false
    override val applyBottomInset: Boolean = false
    override val screenName: String = ScreenKeys.SPLASH

    @Parcelize
    data class Args(
        val payload: DeeplinkPayload?
    ) : Parcelable

    companion object {

        fun create(payload: DeeplinkPayload?) = SplashFragment().withArgs(Args(payload))
    }
}
