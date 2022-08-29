package ru.babaetskv.passionwoman.data.database.entity.transformations

import org.junit.Assert
import org.junit.Test

class AssetDbFormatterTest {
    private val formatter = AssetDbFormatter

    @Test
    fun formatAssetDbPath_returnsFormattedPath() {
        val result = formatter.formatAssetDbPath("static/image/image.jpg")

        Assert.assertEquals("asset:///demo_db_editor/static/image/image.jpg", result)
    }
}
