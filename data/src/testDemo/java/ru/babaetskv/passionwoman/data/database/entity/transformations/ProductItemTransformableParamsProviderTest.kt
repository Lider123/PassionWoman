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
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import ru.babaetskv.passionwoman.data.database.PassionWomanDatabase
import ru.babaetskv.passionwoman.data.database.dao.ColorDao
import ru.babaetskv.passionwoman.data.database.dao.ProductImageDao
import ru.babaetskv.passionwoman.data.database.dao.ProductSizeDao
import ru.babaetskv.passionwoman.data.database.entity.ColorEntity
import java.lang.IllegalStateException

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class ProductItemTransformableParamsProviderTest {
    @Mock
    lateinit var databaseMock: PassionWomanDatabase
    @Mock
    lateinit var imageDaoMock: ProductImageDao
    @Mock
    lateinit var colorDaoMock: ColorDao
    @Mock
    lateinit var productSizeDaoMock: ProductSizeDao
    @InjectMocks
    lateinit var provider: ProductItemTransformableParamsProvider

    @Before
    fun before() {
        whenever(databaseMock.productImageDao).doReturn(imageDaoMock)
        whenever(databaseMock.colorDao).doReturn(colorDaoMock)
        whenever(databaseMock.productSizeDao).doReturn(productSizeDaoMock)
    }

    @Test
    fun provideImages_callsImageDao() = runTest {
        provider.provideImages(1)

        verify(imageDaoMock, times(1)).getForProductItem(1)
    }

    @Test
    fun provideImages_returnsEmptyList_whenThereAreNoImagesInTheDatabase() = runTest {
        whenever(imageDaoMock.getForProductItem(1)).doReturn(emptyList())

        val result = provider.provideImages(1)

        assertTrue(result.isEmpty())
    }

    @Test
    fun provideImages_returnsImagePathsList_whenThereAreImagesInTheDatabase() = runTest {
        whenever(imageDaoMock.getForProductItem(1)).doReturn(listOf("path"))

        val result = provider.provideImages(1)

        assertEquals(listOf("path"), result)
    }

    @Test
    fun provideColor_callsColorDao() = runTest {
        provider.provideColor(1)

        verify(colorDaoMock, times(1)).getById(1)
    }

    @Test
    fun provideColor_returnsColor_whenThereIsColorWithRequiredIdInTheDatabase() = runTest {
        val colorEntity = ColorEntity(
            id = 1,
            uiName = "color 1",
            hex = "hex1"
        )
        whenever(colorDaoMock.getById(1)).doReturn(colorEntity)

        val result = provider.provideColor(1)

        assertEquals(colorEntity, result)
    }

    @Test
    fun provideColor_throwsIllegalStateException_whenThereIsNoColorWithRequiredIdInTheDatabase() =
        runTest {
            whenever(colorDaoMock.getById(1)).doReturn(null)

            runCatching {
                provider.provideColor(1)
            }.onFailure {
                assertTrue(it is IllegalStateException)
            }.onSuccess {
                fail()
            }
        }

    @Test
    fun provideSizes_callsProductSizeDao() = runTest {
        provider.provideSizes(1)

        verify(productSizeDaoMock, times(1)).getForProductItem(1)
    }

    @Test
    fun provideSizes_returnsNull_whenThereAreNoSizesInTheDatabase() = runTest {
        whenever(productSizeDaoMock.getForProductItem(1)).doReturn(emptyList())

        val result = provider.provideSizes(1)

        assertNull(result)
    }

    @Test
    fun provideSizes_returnsSizes_whenThereAreSizesInTheDatabase() = runTest {
        val sizesMock = listOf("size1", "size2")
        whenever(productSizeDaoMock.getForProductItem(1)).doReturn(sizesMock)

        val result = provider.provideSizes(1)

        assertEquals(sizesMock, result)
    }

    @Test
    fun provideAvailableSizes_callsProductSizeDao() = runTest {
        provider.provideAvailableSizes(1)

        verify(productSizeDaoMock, times(1)).getAvailableForProductItem(1)
    }

    @Test
    fun provideAvailableSizes_returnsNull_whenThereAreNoAvailableSizesInTheDatabase() = runTest {
        whenever(productSizeDaoMock.getAvailableForProductItem(1)).doReturn(emptyList())

        val result = provider.provideAvailableSizes(1)

        assertNull(result)
    }

    @Test
    fun provideAvailableSizes_returnsAvailableSizes_whenThereAreAvailableSizesInTheDatabase() =
        runTest {
            val sizesMock = listOf("size1", "size2")
            whenever(productSizeDaoMock.getAvailableForProductItem(1)).doReturn(sizesMock)

            val result = provider.provideAvailableSizes(1)

            assertEquals(sizesMock, result)
        }
}
