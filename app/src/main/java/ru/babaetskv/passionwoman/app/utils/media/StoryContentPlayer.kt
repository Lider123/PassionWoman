package ru.babaetskv.passionwoman.app.utils.media

import android.content.Context
import android.widget.ImageView
import com.google.android.exoplayer2.ui.PlayerView
import ru.babaetskv.passionwoman.app.presentation.view.StoryContentView
import ru.babaetskv.passionwoman.domain.model.Image
import ru.babaetskv.passionwoman.domain.model.Story
import ru.babaetskv.passionwoman.domain.model.Video

class StoryContentPlayer(context: Context) : MediaPlayer<Story.Content, StoryContentView> {
    private val imagePlayer: MediaPlayer<Image, ImageView> = ImageTimerPlayer(IMAGE_PLAYER_DURATION)
    private val videoPlayer: MediaPlayer<Video, PlayerView> = VideoPlayer(context)
    private var currContent: Story.Content? = null
    private val currPlayer: MediaPlayer<*, *>?
        get() = when (currContent) {
            is Story.Content.Image -> imagePlayer
            is Story.Content.Video -> videoPlayer
            null -> null
        }

    override val isPlaying: Boolean
        get() = currPlayer?.isPlaying ?: false

    override fun prepare(content: Story.Content, view: StoryContentView) {
        when (content) {
            is Story.Content.Image -> {
                videoPlayer.reset()
                imagePlayer.prepare(content.image, view.showImageView())
            }
            is Story.Content.Video -> {
                imagePlayer.reset()
                videoPlayer.prepare(content.video, view.showPlayerView())
            }
        }
        currContent = content
    }

    override fun setProgressListener(listener: MediaPlayer.ProgressListener) {
        imagePlayer.setProgressListener(listener)
        videoPlayer.setProgressListener(listener)
    }

    override fun removeProgressListener() {
        imagePlayer.removeProgressListener()
        videoPlayer.removeProgressListener()
    }

    override fun play() {
        currPlayer?.play()
    }

    override fun pause() {
        currPlayer?.pause()
    }

    override fun stop() {
        currPlayer?.stop()
    }

    override fun reset() {
        currPlayer?.reset()
    }

    override fun destroy() {
        currPlayer?.destroy()
    }

    companion object {
        private const val IMAGE_PLAYER_DURATION = 6000L
    }
}
