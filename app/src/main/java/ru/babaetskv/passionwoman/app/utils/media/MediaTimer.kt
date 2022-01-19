package ru.babaetskv.passionwoman.app.utils.media

import android.os.Handler
import android.os.Looper
import kotlin.math.min

class MediaTimer(
    private val durationMs: Long,
    private val listener: MediaPlayer.ProgressListener?
) {
    private var progress: Long = 0
    private val interval = min(10L, durationMs)
    private val handler: Handler = Handler(Looper.getMainLooper())
    private val progressRunnable: Runnable = Runnable(::progressTask)

    var isPlaying: Boolean = false
        private set
    val isEnded: Boolean
        get() = progress >= durationMs

    private fun progressTask() {
        progress = min(durationMs, progress + interval)
        listener?.onProgressUpdated(progress, durationMs)
        if (progress == durationMs) {
            listener?.onEndReached()
            isPlaying = false
        } else {
            handler.postDelayed(progressRunnable, interval)
        }
    }

    fun play() {
        if (isPlaying || isEnded) return

        handler.postDelayed(progressRunnable, interval)
        isPlaying = true
    }

    fun pause() {
        handler.removeCallbacksAndMessages(null)
        isPlaying = false
    }

    fun stop() {
        pause()
        progress = 0
    }
}
