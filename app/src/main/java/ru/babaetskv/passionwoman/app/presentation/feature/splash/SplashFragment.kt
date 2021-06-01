package ru.babaetskv.passionwoman.app.presentation.feature.splash

import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.babaetskv.passionwoman.app.R
import ru.babaetskv.passionwoman.app.presentation.base.BaseFragment

class SplashFragment : BaseFragment<SplashViewModel, BaseFragment.NoArgs>() {
    override val viewModel: SplashViewModel by viewModel()
    override val layoutRes: Int = R.layout.fragment_splash

    companion object {

        fun create() = SplashFragment()
    }
}
