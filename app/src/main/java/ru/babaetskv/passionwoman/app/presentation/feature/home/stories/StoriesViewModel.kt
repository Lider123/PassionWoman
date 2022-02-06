package ru.babaetskv.passionwoman.app.presentation.feature.home.stories

import androidx.lifecycle.MutableLiveData
import ru.babaetskv.passionwoman.app.presentation.base.BaseViewModel
import ru.babaetskv.passionwoman.app.presentation.event.RouterEvent
import ru.babaetskv.passionwoman.app.presentation.base.ViewModelDependencies

class StoriesViewModel(
    args: StoriesFragment.Args,
    dependencies: ViewModelDependencies
) : BaseViewModel<StoriesViewModel.Router>(dependencies) {
    val storiesLiveData = MutableLiveData(args.stories)
    val currStoryIndexLiveData = MutableLiveData(args.initialStoryIndex)

    fun onCurrentStoryChanged(position: Int) {
        currStoryIndexLiveData.postValue(position)
    }

    fun onPrevStoryPressed() {
        val currIndex = currStoryIndexLiveData.value ?: return

        if (currIndex > 0) {
            currStoryIndexLiveData.postValue(currIndex - 1)
        } else onBackPressed()
    }

    fun onNextStoryPressed() {
        val currIndex = currStoryIndexLiveData.value ?: return

        val lastIndex = storiesLiveData.value?.lastIndex ?: return

        if (currIndex < lastIndex) {
            currStoryIndexLiveData.postValue(currIndex + 1)
        } else onBackPressed()
    }

    sealed class Router : RouterEvent
}
