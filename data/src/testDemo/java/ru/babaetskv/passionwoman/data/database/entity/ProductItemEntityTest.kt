package ru.babaetskv.passionwoman.data.database.entity

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.whenever
import ru.babaetskv.passionwoman.data.model.ColorModel

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class ProductItemEntityTest {
    @Mock
    private lateinit var transformableParamsProvider: ProductItemEntity.TransformableParamsProvider

    private val productItem = ProductItemEntity(
        id = 1,
        colorId = 1,
        productId = 1
    )

    @Before
    fun before() = runTest {
        val color = ColorModel(
            id = 1,
            uiName = "color 1",
            hex = "hex1"
        )
        whenever(transformableParamsProvider.provideColor(1)) doReturn color
        whenever(transformableParamsProvider.provideImages(1)) doReturn emptyList()
    }

    @Test
    fun transform_returnsModelWithColorWithRequiredId() = runTest {
        val color = ColorModel(
            id = 1,
            uiName = "color 1",
            hex = "hex1"
        )
        whenever(transformableParamsProvider.provideColor(1)) doReturn color

        val result = productItem.transform(transformableParamsProvider)

        assertEquals(color, result.color)
    }

    @Test
    fun transform_returnsModelWithRequiredSizes() = runTest {
        val sizes = listOf(
            "size1",
            "size2"
        )
        whenever(transformableParamsProvider.provideSizes(1)) doReturn sizes

        val result = productItem.transform(transformableParamsProvider)

        assertEquals(sizes, result.sizes)
    }

    @Test
    fun transform_returnsModelWithRequiredAvailableSizes() = runTest {
        val sizes = listOf(
            "size1",
            "size2"
        )
        whenever(transformableParamsProvider.provideAvailableSizes(1)) doReturn sizes

        val result = productItem.transform(transformableParamsProvider)

        assertEquals(sizes, result.availableSizes)
    }

    @Test
    fun transform_returnsModelWithRequiredImages() = runTest {
        val images = listOf(
            "asset:///demo_db_editor/static/image/product_item_1_1.jpg",
            "asset:///demo_db_editor/static/image/product_item_1_2.jpg"
        )
        whenever(transformableParamsProvider.provideImages(1)) doReturn images

        val result = productItem.transform(transformableParamsProvider)

        assertEquals(images, result.images)
    }
}
