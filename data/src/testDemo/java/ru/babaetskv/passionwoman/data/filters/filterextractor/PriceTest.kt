package ru.babaetskv.passionwoman.data.filters.filterextractor

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.*
import ru.babaetskv.passionwoman.data.database.PassionWomanDatabase
import ru.babaetskv.passionwoman.data.database.dao.ProductDao
import ru.babaetskv.passionwoman.data.filters.FilterExtractor

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class PriceTest {
    @Mock
    private lateinit var databaseMock: PassionWomanDatabase
    @Mock
    private lateinit var productDaoMock: ProductDao

    private val filterModel = FilterExtractor.Price()

    @Before
    fun before() = runTest {
        whenever(databaseMock.productDao).doReturn(productDaoMock)
    }

    @Test
    fun extractAsJson_callsProductDao() = runTest {
        whenever(productDaoMock.getMinPrice()).doReturn(1f)
        whenever(productDaoMock.getMaxPrice()).doReturn(10f)

        filterModel.extractAsJson(databaseMock)

        verify(productDaoMock, times(1)).getMinPrice()
        verify(productDaoMock, times(1)).getMaxPrice()
    }

    @Test
    fun extractAsJson_returnsValues() = runTest {
        whenever(productDaoMock.getMinPrice()).doReturn(1.501f)
        whenever(productDaoMock.getMaxPrice()).doReturn(10.904f)

        val result = filterModel.extractAsJson(databaseMock)

        assertEquals("range", result.getString("type"))
        assertEquals("price", result.getString("code"))
        assertEquals("Price", result.getString("uiName"))
        assertEquals(2, result.getInt("priority"))
        assertEquals(1.50, result.getDouble("minValue"), 1e-2)
        assertEquals(10.90, result.getDouble("maxValue"), 1e-2)
    }

    @Test
    fun extractAsJson_returnsZerosAsMinAndMaxPrices_whenThereAreNoMinAndMaxPricesInTheDatabase() =
        runTest {
            whenever(productDaoMock.getMinPrice()).doReturn(null)
            whenever(productDaoMock.getMaxPrice()).doReturn(null)

            val result = filterModel.extractAsJson(databaseMock)

            assertEquals("range", result.getString("type"))
            assertEquals("price", result.getString("code"))
            assertEquals("Price", result.getString("uiName"))
            assertEquals(2, result.getInt("priority"))
            assertEquals(0.0, result.getDouble("minValue"), 1e-2)
            assertEquals(0.0, result.getDouble("maxValue"), 1e-2)
        }
}
