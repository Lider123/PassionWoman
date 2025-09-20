package ru.babaetskv.passionwoman.data.filters.filterextractor

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.json.JSONArray
import org.junit.Assert.*
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
import ru.babaetskv.passionwoman.data.filters.FilterExtractor

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class CountryTest {
    @Mock
    private lateinit var databaseMock: PassionWomanDatabase
    @Mock
    private lateinit var productCountryDaoMock: ProductCountryDao

    private val filterModel = FilterExtractor.Country()

    private fun createCountry(id: Int) =
        ProductCountryEntity(
            code = "country$id",
            uiName = "Country $id"
        )

    @Before
    fun before() = runTest {
        whenever(databaseMock.productCountryDao).doReturn(productCountryDaoMock)
        whenever(productCountryDaoMock.getAll()).doReturn(emptyList())
    }

    @Test
    fun extractAsJson_callsProductCountryDao() = runTest {
        filterModel.extractAsJson(databaseMock)

        verify(productCountryDaoMock, times(1)).getAll()
    }

    @Test
    fun extractAsJson_returnsBaseValues() = runTest {
        val result = filterModel.extractAsJson(databaseMock)

        assertEquals("multi", result.getString("type"))
        assertEquals("country", result.getString("code"))
        assertEquals("Country", result.getString("uiName"))
        assertEquals(4, result.getInt("priority"))
    }

    @Test
    fun extractAsJson_returnsEmptyValues_whenDaoIsEmpty() = runTest {
        val result = filterModel.extractAsJson(databaseMock)

        assertEquals("[]", result.getJSONArray("values").toString())
    }

    @Test
    fun extractAsJson_returnsCategories_whenDaoIsNotEmpty() = runTest {
        val country = createCountry(1)
        whenever(productCountryDaoMock.getAll()).doReturn(listOf(country))
        val expectedValuesJson = """
            [
                {
                    "code": "${country.code}",
                    "uiName": "${country.uiName}"
                }
            ]
        """.let(::JSONArray)

        val result = filterModel.extractAsJson(databaseMock)

        assertEquals(expectedValuesJson.toString(), result.getJSONArray("values").toString())
    }
}
