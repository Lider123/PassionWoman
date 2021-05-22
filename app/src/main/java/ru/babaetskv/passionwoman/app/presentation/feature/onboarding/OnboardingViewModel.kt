package ru.babaetskv.passionwoman.app.presentation.feature.onboarding

import androidx.lifecycle.MutableLiveData
import com.github.terrakok.cicerone.Router
import ru.babaetskv.passionwoman.app.R
import ru.babaetskv.passionwoman.app.Screens
import ru.babaetskv.passionwoman.app.presentation.base.BaseViewModel
import ru.babaetskv.passionwoman.app.utils.notifier.Notifier
import ru.babaetskv.passionwoman.data.preferences.Preferences

class OnboardingViewModel(
    private val preferences: Preferences,
    notifier: Notifier,
    router: Router
) : BaseViewModel(notifier, router) {
    private val pages = listOf(
        OnboardingPage(R.drawable.onboarding_1, R.string.onboarding_1),
        OnboardingPage(R.drawable.onboarding_2, R.string.onboarding_2),
        OnboardingPage(R.drawable.onboarding_3, R.string.onboarding_3),
        OnboardingPage(R.drawable.onboarding_4, R.string.onboarding_4),
        OnboardingPage(R.drawable.onboarding_5, R.string.onboarding_5,
            actionRes = R.string.onboarding_next,
            actionCallback = ::onNextPressed
        )
    )

    val pagesLiveData = MutableLiveData(pages)

    init {
        preferences.onboardingShowed = true
    }

    private fun onNextPressed() {
        router.replaceScreen(Screens.navigation())
    }
}
