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
import ru.babaetskv.passionwoman.data.model.BrandModel
import ru.babaetskv.passionwoman.data.model.CategoryModel
import ru.babaetskv.passionwoman.data.model.ColorModel
import ru.babaetskv.passionwoman.data.model.ProductItemModel

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class ProductEntityTest {
    @Mock
    private lateinit var transformableParamsProviderMock: ProductEntity.TransformableParamsProvider

    private val product = ProductEntity(
        id = 1,
        categoryId = 1,
        name = "Product 1",
        previewPath = "static/image/product_1_preview_path.jpg",
        price = 10f,
        priceWithDiscount = 9f,
        rating = 4.5f,
        description = "Product 1 description",
        brandId = 1
    )

    @Before
    fun before() = runTest {
        val category = CategoryModel(
            id = 1,
            name = "Category 1",
            image = "asset:///demo_db_editor/static/image/category_1.jpg"
        )
        whenever(transformableParamsProviderMock.provideCategory(1)) doReturn category
        whenever(transformableParamsProviderMock.provideProductItems(1)) doReturn emptyList()
    }

    @Test
    fun transform_returnsModelWithTheSameId() = runTest {
        val result = product.transform(transformableParamsProviderMock)

        assertEquals(1, result.id)
    }

    @Test
    fun transform_returnsModelWithCategoryWithRequiredCategoryId() = runTest {
        val category = CategoryModel(
            id = 1,
            name = "Category 1",
            image = "asset:///demo_db_editor/static/image/category_1.jpg"
        )
        whenever(transformableParamsProviderMock.provideCategory(1)) doReturn category

        val result = product.transform(transformableParamsProviderMock)

        assertEquals(category, result.category)
    }

    @Test
    fun transform_returnsModelWithTheSameName() = runTest {
        val result = product.transform(transformableParamsProviderMock)

        assertEquals("Product 1", result.name)
    }

    @Test
    fun transform_returnsModelWithFormattedPreviewPath() = runTest {
        val result = product.transform(transformableParamsProviderMock)

        assertEquals(
            "asset:///demo_db_editor/static/image/product_1_preview_path.jpg",
            result.preview
        )
    }

    @Test
    fun transform_returnsModelWithTheSamePrice() = runTest {
        val result = product.transform(transformableParamsProviderMock)

        assertEquals(10f, result.price)
    }

    @Test
    fun transform_returnsModelWithTheSamePriceWithDiscount() = runTest {
        val result = product.transform(transformableParamsProviderMock)

        assertEquals(9f, result.priceWithDiscount)
    }

    @Test
    fun transform_returnsModelWithTheSameRating() = runTest {
        val result = product.transform(transformableParamsProviderMock)

        assertEquals(4.5f, result.rating)
    }

    @Test
    fun transform_returnsModelWithTheSameDescription() = runTest {
        val result = product.transform(transformableParamsProviderMock)

        assertEquals("Product 1 description", result.description)
    }

    @Test
    fun transform_returnsModelWithNullDescription_whenDescriptionIsNull() = runTest {
        val result = product.copy(
            description = null
        )
            .transform(transformableParamsProviderMock)

        assertNull(result.description)
    }

    @Test
    fun transform_returnsModelWithTheSameBrand() = runTest {
        val brand = BrandModel(
            id = 1,
            name = "Brand 1",
            logoPath = "asset:///demo_db_editor/static/image/brand_1.jpg"
        )
        whenever(transformableParamsProviderMock.provideBrand(1)) doReturn brand

        val result = product.transform(transformableParamsProviderMock)

        assertEquals(brand, result.brand)
    }

    @Test
    fun transform_returnsModelWithNullBrand_whenBrandIdIsNull() = runTest {
        val result = product.copy(
            brandId = null
        )
            .transform(transformableParamsProviderMock)

        assertNull(result.brand)
    }
    @Test
    fun transform_returnsModelWithItems() = runTest {
        val items = listOf(
            ProductItemModel(
                color = ColorModel(
                    id = 1,
                    uiName = "color 1",
                    hex = "hex1"
                ),
                sizes = emptyList(),
                availableSizes = emptyList(),
                images = emptyList()
            )
        )
        whenever(transformableParamsProviderMock.provideProductItems(1)) doReturn items

        val result = product.transform(transformableParamsProviderMock)

        assertEquals(items, result.items)
    }
}
