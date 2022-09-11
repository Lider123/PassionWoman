package ru.babaetskv.passionwoman.data.database.entity

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import ru.babaetskv.passionwoman.domain.model.base.Transformable.Companion.transform

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class PromotionEntityTest {
    private val promotion = PromotionEntity(
        id = 1,
        imagePath = "static/image/promotion_1.jpg"
    )

    @Test
    fun transform_returnsModelWithTheSameId() = runTest {
        val result = promotion.transform()

        assertEquals(1, result.id)
    }

    @Test
    fun transform_returnsModelWithFormattedImagePath() = runTest {
        val result = promotion.transform()

        assertEquals(
            "file:///android_asset/demo_db_editor/static/image/promotion_1.jpg",
            result.banner
        )
    }
}
