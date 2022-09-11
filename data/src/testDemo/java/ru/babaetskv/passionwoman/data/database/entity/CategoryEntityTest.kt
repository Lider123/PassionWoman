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
class CategoryEntityTest {
    private val category = CategoryEntity(
        id = 1,
        imagePath = "static/image/category_1_image.jpg",
        name = "Category 1"
    )

    @Test
    fun transform_returnsModelWithTheSameId() = runTest {
        val result = category.transform()

        assertEquals(1, result.id)
    }

    @Test
    fun transform_returnsModelWithTheSameName() = runTest {
        val result = category.transform()

        assertEquals("Category 1", result.name)
    }

    @Test
    fun transform_returnsModelWithImagePathWithPrefix() = runTest {
        val result = category.transform()

        assertEquals(
            "file:///android_asset/demo_db_editor/static/image/category_1_image.jpg",
            result.image
        )
    }
}