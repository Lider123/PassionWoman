package ru.babaetskv.passionwoman.app.presentation.feature.onboarding

import androidx.lifecycle.LiveData
import ru.babaetskv.passionwoman.app.presentation.base.IViewModel
import ru.babaetskv.passionwoman.app.presentation.event.RouterEvent

interface OnboardingViewModel : IViewModel {
    val pagesLiveData: LiveData<List<OnboardingPage>>
    val currPageLiveData: LiveData<Int>

    fun onCurrPageChanged(page: Int)
    fun onPrevPagePressed()
    fun onNextPagePressed()

    sealed class Router : RouterEvent {
        object AuthScreen : Router()
    }
}
