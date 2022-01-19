package ru.babaetskv.passionwoman.app.utils.media

import android.content.Context
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.ui.PlayerView
import ru.babaetskv.passionwoman.domain.model.Video

class VideoPlayerImpl(context: Context) : VideoPlayer(context) {

    override fun prepare(content: Video, view: PlayerView) {
        view.player = exoPlayer
        val uri = "asset:///${content.src}"
        val mediaItem = MediaItem.Builder()
            .setUri(uri)
            .build()
        exoPlayer.addMediaItem(mediaItem)
        exoPlayer.prepare()
        exoPlayer.seekTo(0)
    }
}
