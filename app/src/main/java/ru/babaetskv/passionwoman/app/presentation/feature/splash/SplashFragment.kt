package ru.babaetskv.passionwoman.app.presentation.feature.splash

import androidx.core.os.bundleOf
import ru.babaetskv.passionwoman.app.R
import ru.babaetskv.passionwoman.app.presentation.base.BaseFragment
import ru.babaetskv.passionwoman.app.utils.viewModel

class SplashFragment : BaseFragment<SplashViewModel>() {
    override val viewModel: SplashViewModel by viewModel()
    override val layoutRes: Int = R.layout.fragment_splash

    companion object {

        fun create() = SplashFragment().apply {
            arguments = bundleOf()
        }
    }
}
