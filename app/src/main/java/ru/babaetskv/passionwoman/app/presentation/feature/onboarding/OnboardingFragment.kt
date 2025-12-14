package ru.babaetskv.passionwoman.app.presentation.feature.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.babaetskv.passionwoman.app.analytics.constants.ScreenKeys
import ru.babaetskv.passionwoman.app.presentation.base.BaseFragment
import ru.babaetskv.passionwoman.app.presentation.base.FragmentComponent
import ru.babaetskv.passionwoman.app.presentation.theme.PassionWomanTheme

class OnboardingFragment : BaseFragment<OnboardingViewModel, FragmentComponent.NoArgs>() {
    override val layoutRes: Int = 0
    override val viewModel: OnboardingViewModel by viewModel<OnboardingViewModelImpl>()
    override val screenName: String = ScreenKeys.ONBOARDING

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(requireContext()).apply {
        setContent {
            PassionWomanTheme {
                OnboardingScreen(viewModel)
            }
        }
    }
    
    companion object {

        fun create() = OnboardingFragment()
    }
}
