package ru.babaetskv.passionwoman.app.presentation.feature.home.stories

import androidx.lifecycle.LiveData
import ru.babaetskv.passionwoman.app.presentation.base.IViewModel
import ru.babaetskv.passionwoman.domain.model.Story

interface StoriesViewModel : IViewModel {
    val storiesLiveData: LiveData<List<Story>>
    val currStoryIndexLiveData: LiveData<Int>

    fun onCurrentStoryChanged(position: Int)
    fun onPrevStoryPressed()
    fun onNextStoryPressed()
}
