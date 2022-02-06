package ru.babaetskv.passionwoman.app.presentation.feature.onboarding

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.launch
import ru.babaetskv.passionwoman.app.R
import ru.babaetskv.passionwoman.app.presentation.base.BaseViewModel
import ru.babaetskv.passionwoman.app.presentation.base.ViewModelDependencies
import ru.babaetskv.passionwoman.domain.preferences.AppPreferences

class OnboardingViewModelImpl(
    appPreferences: AppPreferences,
    dependencies: ViewModelDependencies
) : BaseViewModel<OnboardingViewModel.Router>(dependencies), OnboardingViewModel {
    private val pages = listOf(
        OnboardingPage(R.drawable.onboarding_1, R.string.onboarding_1),
        OnboardingPage(R.drawable.onboarding_2, R.string.onboarding_2),
        OnboardingPage(R.drawable.onboarding_3, R.string.onboarding_3),
        OnboardingPage(R.drawable.onboarding_4, R.string.onboarding_4),
        OnboardingPage(
            R.drawable.onboarding_5, R.string.onboarding_5,
            actionRes = R.string.onboarding_next,
            actionCallback = ::onNextPressed
        )
    )

    override val pagesLiveData = MutableLiveData(pages)
    override val currPageLiveData = MutableLiveData(0)

    init {
        appPreferences.onboardingShowed = true
    }

    override fun onCurrPageChanged(page: Int) {
        currPageLiveData.postValue(page)
    }

    override fun onPrevPagePressed() {
        val currPage = currPageLiveData.value!!
        if (currPage == 0) return

        currPageLiveData.postValue(currPage - 1)
    }

    override fun onNextPagePressed() {
        val currPage = currPageLiveData.value!!
        if (currPage == pagesLiveData.value!!.size - 1) return

        currPageLiveData.postValue(currPage + 1)
    }

    private fun onNextPressed() {
        launch {
            navigateTo(OnboardingViewModel.Router.AuthScreen)
        }
    }
}
