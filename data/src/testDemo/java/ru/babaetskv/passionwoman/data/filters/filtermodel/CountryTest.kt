package ru.babaetskv.passionwoman.data.filters.filtermodel

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.json.JSONArray
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
import ru.babaetskv.passionwoman.data.database.dao.ProductCountryDao
import ru.babaetskv.passionwoman.data.database.entity.ProductCountryEntity
import ru.babaetskv.passionwoman.data.filters.FilterModel

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class CountryTest {
    @Mock
    lateinit var databaseMock: PassionWomanDatabase
    @Mock
    lateinit var productCountryDaoMock: ProductCountryDao

    private val filterModel = FilterModel.Country

    @Before
    fun before() = runTest {
        whenever(databaseMock.productCountryDao).doReturn(productCountryDaoMock)
        whenever(productCountryDaoMock.getAll()).doReturn(emptyList())
    }

    @Test
    fun toJson_callsProductCountryDao() = runTest {
        filterModel.toJson(databaseMock)

        verify(productCountryDaoMock, times(1)).getAll()
    }

    @Test
    fun toJson_returnsBaseValues() = runTest {
        val result = filterModel.toJson(databaseMock)

        Assert.assertEquals("multi", result.getString("type"))
        Assert.assertEquals("country", result.getString("code"))
        Assert.assertEquals("Country", result.getString("uiName"))
        Assert.assertEquals(4, result.getInt("priority"))
    }

    @Test
    fun toJson_returnsEmptyValues_whenDaoIsEmpty() = runTest {
        val result = filterModel.toJson(databaseMock)

        Assert.assertEquals("[]", result.getJSONArray("values").toString())
    }

    @Test
    fun toJson_returnsCategories_whenDaoIsNotEmpty() = runTest {
        val countryEntity = ProductCountryEntity(
            code = "country1",
            uiName = "Country 1"
        )
        whenever(productCountryDaoMock.getAll()).doReturn(listOf(countryEntity))
        val expectedValuesJson = """
            [
                {
                    "code": "country1",
                    "uiName": "Country 1"
                }
            ]
        """.let(::JSONArray)

        val result = filterModel.toJson(databaseMock)

        Assert.assertEquals(expectedValuesJson.toString(), result.getJSONArray("values").toString())
    }
}
