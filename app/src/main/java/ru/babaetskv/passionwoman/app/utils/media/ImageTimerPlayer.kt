package ru.babaetskv.passionwoman.app.utils.media

import android.widget.ImageView
import ru.babaetskv.passionwoman.app.R
import ru.babaetskv.passionwoman.app.utils.load
import ru.babaetskv.passionwoman.domain.model.Image

/**
 * Player which is needed to imitate video by showing an image for defined period of time
 *
 * @param playDuration - duration of image showing in milliseconds
 */
class ImageTimerPlayer(
    private val playDuration: Long
) : MediaPlayer<Image, ImageView> {
    private var timer: MediaTimer? = null
    private var mProgressListener: MediaPlayer.ProgressListener? = null

    override val isPlaying: Boolean
        get() = timer?.isPlaying ?: false

    override fun prepare(content: Image, view: ImageView) {
        view.load(content, R.drawable.photo_placeholder)
        timer?.stop()
        timer = MediaTimer(playDuration, mProgressListener)
    }

    override fun setProgressListener(listener: MediaPlayer.ProgressListener) {
        mProgressListener = listener
    }

    override fun removeProgressListener() {
        mProgressListener = null
    }

    override fun play() {
        timer?.play()
    }

    override fun pause() {
        timer?.pause()
    }

    override fun stop() {
        timer?.stop()
    }

    override fun destroy() {
        timer?.stop()
    }

    override fun reset() {
        destroy()
    }
}
