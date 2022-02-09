package ru.babaetskv.passionwoman.app.presentation.feature.home.stories

import androidx.lifecycle.MutableLiveData
import ru.babaetskv.passionwoman.app.presentation.base.BaseViewModel
import ru.babaetskv.passionwoman.app.presentation.base.ViewModelDependencies

class StoriesViewModelImpl(
    args: StoriesFragment.Args,
    dependencies: ViewModelDependencies
) : BaseViewModel<StoriesViewModel.Router>(dependencies), StoriesViewModel {
    override val storiesLiveData = MutableLiveData(args.stories)
    override val currStoryIndexLiveData = MutableLiveData(args.initialStoryIndex)

    override fun onCurrentStoryChanged(position: Int) {
        currStoryIndexLiveData.postValue(position)
    }

    override fun onPrevStoryPressed() {
        val currIndex = currStoryIndexLiveData.value ?: return

        if (currIndex > 0) {
            currStoryIndexLiveData.postValue(currIndex - 1)
        } else onBackPressed()
    }

    override fun onNextStoryPressed() {
        val currIndex = currStoryIndexLiveData.value ?: return

        val lastIndex = storiesLiveData.value?.lastIndex ?: return

        if (currIndex < lastIndex) {
            currStoryIndexLiveData.postValue(currIndex + 1)
        } else onBackPressed()
    }
}
