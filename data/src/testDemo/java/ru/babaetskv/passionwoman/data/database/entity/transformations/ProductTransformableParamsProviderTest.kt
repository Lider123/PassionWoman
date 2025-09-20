package ru.babaetskv.passionwoman.data.database.entity.transformations

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.*
import retrofit2.HttpException
import ru.babaetskv.passionwoman.data.api.exception.ApiExceptionProvider
import ru.babaetskv.passionwoman.data.database.PassionWomanDatabase
import ru.babaetskv.passionwoman.data.database.dao.*
import ru.babaetskv.passionwoman.data.database.entity.BrandEntity
import ru.babaetskv.passionwoman.data.database.entity.CategoryEntity
import ru.babaetskv.passionwoman.data.database.entity.ProductItemEntity
import ru.babaetskv.passionwoman.data.model.BrandModel
import ru.babaetskv.passionwoman.data.model.CategoryModel
import ru.babaetskv.passionwoman.data.model.ColorModel
import ru.babaetskv.passionwoman.data.model.ProductItemModel

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class ProductTransformableParamsProviderTest {
    @Mock
    private lateinit var databaseMock: PassionWomanDatabase
    @Mock
    private lateinit var exceptionProviderMock: ApiExceptionProvider
    @Mock
    private lateinit var productItemTransformableParamsProviderMock: ProductItemEntity.TransformableParamsProvider
    @InjectMocks
    private lateinit var provider: ProductTransformableParamsProvider

    private val categoryDaoMock: CategoryDao = mock()
    private val productItemDaoMock: ProductItemDao = mock()
    private val brandDaoMock: BrandDao = mock()
    private val productCountryDaoMock: ProductCountryDao = mock {
        onBlocking { getCodesForProduct(any()) } doReturn emptyList()
    }
    private val productModelDaoMock: ProductModelDao = mock {
        onBlocking { getCodesForProduct(any()) } doReturn emptyList()
    }
    private val productMaterialDaoMock: ProductMaterialDao = mock {
        onBlocking { getCodesForProduct(any()) } doReturn emptyList()
    }
    private val productSeasonDaoMock: ProductSeasonDao = mock {
        onBlocking { getCodesForProduct(any()) } doReturn emptyList()
    }
    private val productStyleDaoMock: ProductStyleDao = mock {
        onBlocking { getCodesForProduct(any()) } doReturn emptyList()
    }
    private val productTypeDaoMock: ProductTypeDao = mock {
        onBlocking { getCodesForProduct(any()) } doReturn emptyList()
    }

    @Before
    fun before() {
        whenever(databaseMock.categoryDao) doReturn categoryDaoMock
        whenever(databaseMock.brandDao) doReturn brandDaoMock
        whenever(databaseMock.productItemDao) doReturn productItemDaoMock
        whenever(databaseMock.productCountryDao) doReturn productCountryDaoMock
        whenever(databaseMock.productModelDao) doReturn productModelDaoMock
        whenever(databaseMock.productMaterialDao) doReturn productMaterialDaoMock
        whenever(databaseMock.productSeasonDao) doReturn productSeasonDaoMock
        whenever(databaseMock.productStyleDao) doReturn productStyleDaoMock
        whenever(databaseMock.productTypeDao) doReturn productTypeDaoMock
    }

    @Test
    fun provideCategory_throwsNotFound_whenThereIsNoCategoryWithRequiredIdInTheDatabase() =
        runTest {
            val exceptionMock: HttpException = mock()
            whenever(exceptionProviderMock.getNotFoundException(any())) doReturn exceptionMock

            try {
                provider.provideCategory(1)
                fail()
            } catch (e: HttpException) {
                verify(exceptionProviderMock, times(1))
                    .getNotFoundException("Cannot find category with id 1")
                assertEquals(e, exceptionMock)
            }
        }

    @Test
    fun provideCategory_callsCategoryDao() = runTest {
        val category = CategoryEntity(
            id = 1,
            name = "Category 1",
            imagePath = "static/image/category_1_image_path.jpg"
        )
        whenever(categoryDaoMock.getById(1)) doReturn category

        provider.provideCategory(1)

        verify(categoryDaoMock, times(1)).getById(1)
    }

    @Test
    fun provideCategory_returnsCategoryWithRequiredId_whenThereIsCategoryWithRequiredIdInTheDatabase() =
        runTest {
            val category = CategoryEntity(
                id = 1,
                name = "Category 1",
                imagePath = "static/image/category_1_image_path.jpg"
            )
            whenever(categoryDaoMock.getById(1)) doReturn category

            val result = provider.provideCategory(1)

            val expected = CategoryModel(
                id = 1,
                name = "Category 1",
                image = "file:///android_asset/demo_db_editor/static/image/category_1_image_path.jpg"
            )
            assertEquals(expected, result)
        }

    @Test
    fun provideBrand_throwsNotFound_whenThereIsNoBrandWithRequiredIdInTheDatabase() =
        runTest {
            val exceptionMock: HttpException = mock()
            whenever(exceptionProviderMock.getNotFoundException(any())) doReturn exceptionMock

            try {
                provider.provideBrand(1)
                fail()
            } catch (e: HttpException) {
                verify(exceptionProviderMock, times(1))
                    .getNotFoundException("Cannot find brand with id 1")
                assertEquals(exceptionMock, e)
            }
        }

    @Test
    fun provideBrand_callsBrandDao() = runTest {
        val brand = BrandEntity(
            id = 1,
            name = "Brand 1",
            logoPath = "static/image/brand_1_logo_path.jpg"
        )
        whenever(brandDaoMock.getById(1)) doReturn brand

        provider.provideBrand(1)

        verify(brandDaoMock, times(1)).getById(1)
    }

    @Test
    fun provideBrand_returnsBrandWithRequiredId_whenThereIsBrandWithRequiredIdInTheDatabase() =
        runTest {
            val brand = BrandEntity(
                id = 1,
                name = "Brand 1",
                logoPath = "static/image/brand_1_logo_path.jpg"
            )
            whenever(brandDaoMock.getById(1)) doReturn brand

            val result = provider.provideBrand(1)

            val expected = BrandModel(
                id = 1,
                name = "Brand 1",
                logoPath = "file:///android_asset/demo_db_editor/static/image/brand_1_logo_path.jpg"
            )
            assertEquals(expected, result)
        }

    @Test
    fun provideProductItems_callsProductItemDao() = runTest {
        val productItem = ProductItemEntity(
            id = 1,
            colorId = 1,
            productId = 1
        )
        val color = ColorModel(
            id = 1,
            uiName = "Color 1",
            hex = "hex1"
        )
        whenever(productItemDaoMock.getByProductId(1)) doReturn listOf(productItem)
        whenever(productItemTransformableParamsProviderMock.provideColor(1)) doReturn color
        whenever(productItemTransformableParamsProviderMock.provideImages(1)) doReturn emptyList()

        provider.provideProductItems(1)

        verify(productItemDaoMock, times(1)).getByProductId(1)
    }

    @Test
    fun provideProductItems_throwNotFound_whenThereAreNoItemsForRequiredProductId() = runTest {
        val exceptionMock: HttpException = mock()
        whenever(productItemDaoMock.getByProductId(1)) doReturn emptyList()
        whenever(exceptionProviderMock.getNotFoundException(any())) doReturn exceptionMock

        try {
            provider.provideProductItems(1)
            fail()
        } catch (e: HttpException) {
            verify(exceptionProviderMock, times(1))
                .getNotFoundException("Cannot find product items for the product with id 1")
            assertEquals(e, exceptionMock)
        }
    }

    @Test
    fun provideProductItems_returnsProductItem_whenThereAreProductItemsWithRequiredId() = runTest {
        val productItems = listOf(
            ProductItemEntity(
                id = 1,
                colorId = 1,
                productId = 1
            ),
            ProductItemEntity(
                id = 2,
                colorId = 2,
                productId = 1
            )
        )
        whenever(productItemDaoMock.getByProductId(1)) doReturn productItems
        whenever(productItemTransformableParamsProviderMock.provideColor(any())).doAnswer { invocation ->
            val colorId = invocation.getArgument<Long>(0)
            ColorModel(
                id = colorId,
                uiName = "Color $colorId",
                hex = "hex$colorId"
            )
        }
        whenever(productItemTransformableParamsProviderMock.provideImages(any())) doReturn emptyList()

        val result = provider.provideProductItems(1)

        val expected = listOf(
            ProductItemModel(
                color = ColorModel(
                    id = 1,
                    uiName = "Color 1",
                    hex = "hex1"
                ),
                images = emptyList(),
                sizes = null,
                availableSizes = null
            ),
            ProductItemModel(
                color = ColorModel(
                    id = 2,
                    uiName = "Color 2",
                    hex = "hex2"
                ),
                images = emptyList(),
                sizes = null,
                availableSizes = null
            )
        )
        assertEquals(expected, result)
    }

    @Test
    fun provideAdditionalInfo_callsProductCountryDao() = runTest {
        provider.provideAdditionalInfo(1)

        verify(productCountryDaoMock, times(1)).getCodesForProduct(1)
    }

    @Test
    fun provideAdditionalInfo_resultDoesNotContainCountriesKey_whenThereAreNoCountriesForRequiredProductIdInTheDatabase() =
        runTest {
            val result = provider.provideAdditionalInfo(1)

            assertFalse(result.containsKey("country"))
        }

    @Test
    fun provideAdditionalInfo_resultContainsCountries_whenThereAreCountriesWithRequiredProductIdInTheDatabase() =
        runTest {
            val countryCodes = listOf(
                "country1",
                "country2"
            )
            whenever(productCountryDaoMock.getCodesForProduct(1)) doReturn countryCodes

            val result = provider.provideAdditionalInfo(1)

            assertEquals(countryCodes, result["country"])
        }

    @Test
    fun provideAdditionalInfo_callsProductModelDao() = runTest {
        provider.provideAdditionalInfo(1)

        verify(productModelDaoMock, times(1)).getCodesForProduct(1)
    }

    @Test
    fun provideAdditionalInfo_resultDoesNotContainModelsKey_whenThereAreNoModelsForRequiredProductIdInTheDatabase() =
        runTest {
            val result = provider.provideAdditionalInfo(1)

            assertFalse(result.containsKey("model"))
        }

    @Test
    fun provideAdditionalInfo_resultContainsModels_whenThereAreModelsWithRequiredProductIdInTheDatabase() =
        runTest {
            val modelCodes = listOf(
                "model1",
                "model2"
            )
            whenever(productModelDaoMock.getCodesForProduct(1)) doReturn modelCodes

            val result = provider.provideAdditionalInfo(1)

            assertEquals(modelCodes, result["model"])
        }

    @Test
    fun provideAdditionalInfo_callsProductMaterialDao() = runTest {
        provider.provideAdditionalInfo(1)

        verify(productMaterialDaoMock, times(1)).getCodesForProduct(1)
    }

    @Test
    fun provideAdditionalInfo_resultDoesNotContainMaterialsKey_whenThereAreNoMaterialsForRequiredProductIdInTheDatabase() =
        runTest {
            val result = provider.provideAdditionalInfo(1)

            assertFalse(result.containsKey("material"))
        }

    @Test
    fun provideAdditionalInfo_resultContainsMaterials_whenThereAreMaterialsWithRequiredProductIdInTheDatabase() =
        runTest {
            val materialCodes = listOf(
                "material1",
                "material2"
            )
            whenever(productMaterialDaoMock.getCodesForProduct(1)) doReturn materialCodes

            val result = provider.provideAdditionalInfo(1)

            assertEquals(materialCodes, result["material"])
        }

    @Test
    fun provideAdditionalInfo_callsProductSeasonDao() = runTest {
        provider.provideAdditionalInfo(1)

        verify(productSeasonDaoMock, times(1)).getCodesForProduct(1)
    }

    @Test
    fun provideAdditionalInfo_resultDoesNotContainSeasonsKey_whenThereAreNoSeasonsForRequiredProductIdInTheDatabase() =
        runTest {
            val result = provider.provideAdditionalInfo(1)

            assertFalse(result.containsKey("season"))
        }

    @Test
    fun provideAdditionalInfo_resultContainsSeasons_whenThereAreSeasonsWithRequiredProductIdInTheDatabase() =
        runTest {
            val seasonCodes = listOf(
                "season1",
                "season2"
            )
            whenever(productSeasonDaoMock.getCodesForProduct(1)) doReturn seasonCodes

            val result = provider.provideAdditionalInfo(1)

            assertEquals(seasonCodes, result["season"])
        }

    @Test
    fun provideAdditionalInfo_callsProductStyleDao() = runTest {
        provider.provideAdditionalInfo(1)

        verify(productStyleDaoMock, times(1)).getCodesForProduct(1)
    }

    @Test
    fun provideAdditionalInfo_resultDoesNotContainStylesKey_whenThereAreNoStylesForRequiredProductIdInTheDatabase() =
        runTest {
            val result = provider.provideAdditionalInfo(1)

            assertFalse(result.containsKey("style"))
        }

    @Test
    fun provideAdditionalInfo_resultContainsStyles_whenThereAreStylesWithRequiredProductIdInTheDatabase() =
        runTest {
            val styleCodes = listOf(
                "style1",
                "style2"
            )
            whenever(productStyleDaoMock.getCodesForProduct(1)) doReturn styleCodes

            val result = provider.provideAdditionalInfo(1)

            assertEquals(styleCodes, result["style"])
        }

    @Test
    fun provideAdditionalInfo_callsProductTypeDao() = runTest {
        provider.provideAdditionalInfo(1)

        verify(productTypeDaoMock, times(1)).getCodesForProduct(1)
    }

    @Test
    fun provideAdditionalInfo_resultDoesNotContainTypesKey_whenThereAreNoTypesForRequiredProductIdInTheDatabase() =
        runTest {
            val result = provider.provideAdditionalInfo(1)

            assertFalse(result.containsKey("type"))
        }

    @Test
    fun provideAdditionalInfo_resultContainsTypes_whenThereAreTypesWithRequiredProductIdInTheDatabase() =
        runTest {
            val typeCodes = listOf(
                "type1",
                "type2"
            )
            whenever(productTypeDaoMock.getCodesForProduct(1)) doReturn typeCodes

            val result = provider.provideAdditionalInfo(1)

            assertEquals(typeCodes, result["type"])
        }
}
