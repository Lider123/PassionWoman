package ru.babaetskv.passionwoman.app.presentation.feature.onboarding

import androidx.lifecycle.MutableLiveData
import ru.babaetskv.passionwoman.app.R
import ru.babaetskv.passionwoman.app.Screens
import ru.babaetskv.passionwoman.app.navigation.AppRouter
import ru.babaetskv.passionwoman.app.presentation.base.BaseViewModel
import ru.babaetskv.passionwoman.app.utils.notifier.Notifier
import ru.babaetskv.passionwoman.domain.preferences.AppPreferences

class OnboardingViewModel(
    appPreferences: AppPreferences,
    notifier: Notifier,
    router: AppRouter
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
    val currPageLiveData = MutableLiveData(0)

    init {
        appPreferences.onboardingShowed = true
    }

    private fun onNextPressed() {
        router.newRootScreen(Screens.auth())
    }

    fun onCurrPageChanged(page: Int) {
        currPageLiveData.postValue(page)
    }

    fun onPrevPagePressed() {
        val currPage = currPageLiveData.value!!
        if (currPage == 0) return

        currPageLiveData.postValue(currPage - 1)
    }

    fun onNextPagePressed() {
        val currPage = currPageLiveData.value!!
        if (currPage == pagesLiveData.value!!.size - 1) return

        currPageLiveData.postValue(currPage + 1)
    }
}
