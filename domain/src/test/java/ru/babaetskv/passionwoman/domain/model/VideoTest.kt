package ru.babaetskv.passionwoman.domain.model

import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.BlockJUnit4ClassRunner

@RunWith(BlockJUnit4ClassRunner::class)
class VideoTest {

    @Test
    fun toString_returnsUrl() {
        val video = Video("video_url")

        Assert.assertEquals(video.toString(), video.url)
    }
}
