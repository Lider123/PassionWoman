package ru.babaetskv.passionwoman.app.utils.media

import android.view.View

interface MediaPlayer<T : Any, V : View> {
    val isPlaying: Boolean

    fun prepare(content: T, view: V)
    fun setProgressListener(listener: ProgressListener)
    fun removeProgressListener()
    fun play()
    fun pause()
    fun stop()
    fun reset()
    fun destroy()

    interface ProgressListener {
        fun onProgressUpdated(progress: Long, duration: Long)
        fun onEndReached()
    }
}
