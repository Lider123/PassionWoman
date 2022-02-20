package ru.babaetskv.passionwoman.app.utils.media

import android.content.Context
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.PlayerView
import ru.babaetskv.passionwoman.domain.model.Video

class VideoPlayer(
    private val context: Context
) : MediaPlayer<Video, PlayerView>, Player.Listener {
    private var mProgressListener: MediaPlayer.ProgressListener? = null
    private var timer: MediaTimer? = null
    private lateinit var exoPlayer: ExoPlayer

    override val isPlaying: Boolean
        get() = exoPlayer.isPlaying

    init {
        init()
    }

    private fun init() {
        exoPlayer = ExoPlayer.Builder(context)
            .build()
        exoPlayer.addListener(this)
    }

    override fun prepare(content: Video, view: PlayerView) {
        view.player = exoPlayer
        val mediaItem = MediaItem.Builder()
            .setUri(content.url)
            .build()
        exoPlayer.addMediaItem(mediaItem)
        exoPlayer.prepare()
        exoPlayer.seekTo(0)
    }

    override fun setProgressListener(listener: MediaPlayer.ProgressListener) {
        mProgressListener = listener
    }

    override fun removeProgressListener() {
        mProgressListener = null
    }

    override fun play() {
        exoPlayer.play()
        timer?.play()
    }

    override fun pause() {
        exoPlayer.pause()
        timer?.pause()
    }

    override fun stop() {
        exoPlayer.pause()
        exoPlayer.seekTo(0)
        timer?.stop()
    }

    override fun destroy() {
        exoPlayer.stop()
        exoPlayer.release()
        timer?.stop()
    }

    override fun reset() {
        destroy()
        init()
    }

    override fun onPlaybackStateChanged(playbackState: Int) {
        super.onPlaybackStateChanged(playbackState)
        when (playbackState) {
            ExoPlayer.STATE_READY -> {
                timer?.stop()
                timer = MediaTimer(exoPlayer.duration, object : MediaPlayer.ProgressListener {

                    override fun onProgressUpdated(progress: Long, duration: Long) {
                        mProgressListener?.onProgressUpdated(exoPlayer.currentPosition, exoPlayer.duration)
                    }

                    override fun onEndReached() = Unit
                })
                if (exoPlayer.playWhenReady) timer?.play()
            }
            ExoPlayer.STATE_ENDED -> {
                mProgressListener?.onEndReached()
                timer?.stop()
            }
        }
    }
}
