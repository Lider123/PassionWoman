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
class BrandEntityTest {
    private val brand = BrandEntity(
        id = 1,
        logoPath = "static/image/brand_1_logo.jpg",
        name = "Brand 1"
    )

    @Test
    fun transform_returnsModelWithTheSameId() = runTest {
        val result = brand.transform()

        assertEquals(1, result.id)
    }

    @Test
    fun transform_returnsModelWithTheSameName() = runTest {
        val result = brand.transform()

        assertEquals("Brand 1", result.name)
    }

    @Test
    fun transform_returnsModelWithLogoPathWithPrefix() = runTest {
        val result = brand.transform()

        assertEquals(
            "file:///android_asset/demo_db_editor/static/image/brand_1_logo.jpg",
            result.logoPath
        )
    }
}
