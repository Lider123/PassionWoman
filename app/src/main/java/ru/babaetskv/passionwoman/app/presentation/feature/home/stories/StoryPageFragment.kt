package ru.babaetskv.passionwoman.app.presentation.feature.home.stories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.core.os.bundleOf
import androidx.core.view.children
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import ru.babaetskv.passionwoman.app.R
import ru.babaetskv.passionwoman.app.databinding.FragmentStoryPageBinding
import ru.babaetskv.passionwoman.app.presentation.view.StoryContentView
import ru.babaetskv.passionwoman.app.utils.dimen
import ru.babaetskv.passionwoman.app.utils.dip
import ru.babaetskv.passionwoman.app.utils.drawable
import ru.babaetskv.passionwoman.app.utils.media.MediaPlayer
import ru.babaetskv.passionwoman.app.utils.media.StoryContentPlayer
import ru.babaetskv.passionwoman.app.utils.setOnSingleClickListener
import ru.babaetskv.passionwoman.domain.model.Story

class StoryPageFragment : Fragment(), MediaPlayer.ProgressListener {
    private val contents: List<Story.Content>
        get() = requireArguments().getParcelable<Story>(ARG_STORY)!!.contents
    private var currentPosition: Int = 0
    private lateinit var contentPlayer: MediaPlayer<Story.Content, StoryContentView>
    private lateinit var binding: FragmentStoryPageBinding

    var clickListener: ClickListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        contentPlayer = StoryContentPlayer(requireContext()).apply {
            setProgressListener(this@StoryPageFragment)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentStoryPageBinding.inflate(inflater, container, false).also {
        binding = it
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.run {
            progressContainer.run {
                removeAllViews()
                contents.forEachIndexed { index, _ ->
                    ProgressBar(context, null, com.google.android.material.R.style.Widget_AppCompat_ProgressBar_Horizontal)
                        .apply {
                            tag = index
                            layoutParams = LinearLayout.LayoutParams(0, dip(4)).apply {
                                weight = 1f
                            }
                            val horizontalPadding = dimen(R.dimen.margin_extra_small)
                            setPadding(horizontalPadding, 0, horizontalPadding, 0)
                            max = MAX_PROGRESS
                            progress = 0
                            progressDrawable = drawable(R.drawable.progress_story_content)
                        }.let(::addView)
                }
            }
            btnClose.setOnSingleClickListener {
                clickListener?.onClosePressed()
            }
            viewPrev.setOnSingleClickListener {
                if (currentPosition > 0) {
                    setContent(--currentPosition)
                } else {
                    clickListener?.onPrevStoryPressed()
                }
            }
            viewNext.setOnSingleClickListener {
                if (currentPosition < contents.lastIndex) {
                    setContent(++currentPosition)
                } else clickListener?.onNextStoryPressed()
            }
        }
        resetContent()
    }

    override fun onResume() {
        super.onResume()
        contentPlayer.play()
    }

    override fun onPause() {
        contentPlayer.reset()
        resetContent()
        super.onPause()
    }

    override fun onDestroy() {
        contentPlayer.destroy()
        super.onDestroy()
    }

    override fun onProgressUpdated(progress: Long, duration: Long) {
        binding.progressContainer.findViewWithTag<ProgressBar>(currentPosition)?.let {
            it.progress = (100f * progress / duration).toInt()
        }
    }

    override fun onEndReached() {
        if (currentPosition < contents.lastIndex) setContent(++currentPosition) else {
            clickListener?.onNextStoryPressed()
        }
    }

    private fun resetContent() {
        currentPosition = 0
        setContent(currentPosition, playImmediately = false)
    }

    private fun setContent(position: Int, playImmediately: Boolean = true) {
        val content = contents[position]
        // TODO: set up borderless story content
        binding.run {
            tvTitle.run {
                isVisible = content.title != null
                text = content.title
            }
            tvText.run {
                isVisible = content.text != null
                text = content.text
            }
        }
        contentPlayer.prepare(content, binding.contentView)
        initContentProgress(position)
        if (playImmediately) contentPlayer.play()
    }

    private fun initContentProgress(position: Int) {
        binding.progressContainer.children.forEach {
            it as ProgressBar
            val childPosition = it.tag as Int
            it.progress = if (childPosition < position) MAX_PROGRESS else 0
        }
    }

    interface ClickListener {
        fun onClosePressed()
        fun onPrevStoryPressed()
        fun onNextStoryPressed()
    }

    companion object {
        private const val ARG_STORY = "arg_story"
        private const val MAX_PROGRESS = 100

        fun create(story: Story) = StoryPageFragment().apply {
            arguments = bundleOf(
                ARG_STORY to story
            )
        }
    }
}
