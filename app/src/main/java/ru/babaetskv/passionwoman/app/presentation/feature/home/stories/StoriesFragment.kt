package ru.babaetskv.passionwoman.app.presentation.feature.home.stories

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.Parcelable
import android.viewbinding.library.fragment.viewBinding
import androidx.viewpager2.widget.ViewPager2
import kotlinx.parcelize.Parcelize
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import ru.babaetskv.passionwoman.app.R
import ru.babaetskv.passionwoman.app.analytics.constants.ScreenKeys
import ru.babaetskv.passionwoman.app.databinding.FragmentStoriesBinding
import ru.babaetskv.passionwoman.app.presentation.base.BaseFragment
import ru.babaetskv.passionwoman.app.utils.view.CubePageTransformer
import ru.babaetskv.passionwoman.domain.model.Story
import kotlin.math.abs

class StoriesFragment :
    BaseFragment<StoriesViewModel, StoriesFragment.Args>() {
    private val binding: FragmentStoriesBinding by viewBinding()
    private val adapter: StoryPagesAdapter by lazy {
        StoryPagesAdapter(this,
            onCloseClickListener = ::onBackPressed,
            onPrevStoryClickListener = viewModel::onPrevStoryPressed,
            onNextStoryClickListener = viewModel::onNextStoryPressed
        )
    }
    private var parentOrientationFlag: Int? = null

    override val layoutRes: Int
        get() = R.layout.fragment_stories
    override val screenName: String
        get() = ScreenKeys.STORIES
    override val viewModel: StoriesViewModel by viewModel<StoriesViewModelImpl> {
        parametersOf(args)
    }

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        parentOrientationFlag = requireActivity().requestedOrientation
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    override fun onStop() {
        parentOrientationFlag?.let(requireActivity()::setRequestedOrientation)
        super.onStop()
    }

    override fun initViews() {
        super.initViews()
        binding.viewPager.run {
            setPageTransformer(
                CubePageTransformer(
                    15f
                )
            )
            adapter = this@StoriesFragment.adapter
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {

                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    viewModel.onCurrentStoryChanged(position)
                }
            })
        }
    }

    override fun initObservers() {
        super.initObservers()
        viewModel.storiesLiveData.observe(this, adapter::submitData)
        viewModel.currStoryIndexLiveData.observe(this) {
            binding.viewPager.run {
                if (currentItem != it) setCurrentItem(it, abs(currentItem - it) < 2)
            }
        }
    }

    @Parcelize
    data class Args(
        val stories: List<Story>,
        val initialStoryIndex: Int
    ) : Parcelable

    companion object {

        fun create(stories: List<Story>, initialStoryIndex: Int) = StoriesFragment().withArgs(Args(
            stories = stories,
            initialStoryIndex = initialStoryIndex
        ))
    }
}
