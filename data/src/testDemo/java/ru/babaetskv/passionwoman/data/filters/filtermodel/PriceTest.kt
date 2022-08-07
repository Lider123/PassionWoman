package ru.babaetskv.passionwoman.data.filters.filtermodel

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import ru.babaetskv.passionwoman.data.database.PassionWomanDatabase
import ru.babaetskv.passionwoman.data.database.dao.ProductDao
import ru.babaetskv.passionwoman.data.filters.FilterModel

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class PriceTest {
    @Mock
    lateinit var databaseMock: PassionWomanDatabase
    @Mock
    lateinit var productDaoMock: ProductDao

    private val filterModel = FilterModel.Price

    @Before
    fun before() = runTest {
        whenever(databaseMock.productDao).doReturn(productDaoMock)
    }

    @Test
    fun toJson_callsProductDao() = runTest {
        filterModel.toJson(databaseMock)

        verify(productDaoMock, times(1)).getMinPrice()
        verify(productDaoMock, times(1)).getMaxPrice()
    }

    @Test
    fun toJson_returnsValues() = runTest {
        whenever(productDaoMock.getMinPrice()).doReturn(1.501f)
        whenever(productDaoMock.getMaxPrice()).doReturn(10.904f)

        val result = filterModel.toJson(databaseMock)

        Assert.assertEquals("range", result.getString("type"))
        Assert.assertEquals("price", result.getString("code"))
        Assert.assertEquals("Price", result.getString("uiName"))
        Assert.assertEquals(2, result.getInt("priority"))
        Assert.assertEquals(1.50f.toDouble(), result.getDouble("minValue"), 1e-2)
        Assert.assertEquals(10.90f.toDouble(), result.getDouble("maxValue"), 1e-2)
    }
}
