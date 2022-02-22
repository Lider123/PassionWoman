package ru.babaetskv.passionwoman.app.presentation.view

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.core.view.isVisible
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.ui.PlayerView
import ru.babaetskv.passionwoman.app.databinding.ViewStoryContentBinding
import ru.babaetskv.passionwoman.app.utils.viewBinding

class StoryContentView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    private val binding: ViewStoryContentBinding =
        viewBinding(ViewStoryContentBinding::inflate, true)

    init {
        binding.playerView.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
    }

    fun showImageView(): ImageView {
        binding.run {
            playerView.isVisible = false
            imgContent.isVisible = true
        }
        return binding.imgContent
    }

    fun showPlayerView(): PlayerView {
        binding.run {
            imgContent.isVisible = false
            playerView.isVisible = true
            playerView.requestFocus()
        }
        return binding.playerView
    }
}
