package ru.babaetskv.passionwoman.app.presentation.feature.onboarding

import androidx.lifecycle.LiveData
import ru.babaetskv.passionwoman.app.presentation.base.IViewModel

interface OnboardingViewModel : IViewModel {
    val pagesLiveData: LiveData<List<OnboardingPage>>
    val currPageLiveData: LiveData<Int>

    fun onCurrPageChanged(page: Int)
    fun onPrevPagePressed()
    fun onNextPagePressed()
}
