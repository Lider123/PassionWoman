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
import ru.babaetskv.passionwoman.data.database.dao.ProductSeasonDao
import ru.babaetskv.passionwoman.data.database.entity.ProductSeasonEntity
import ru.babaetskv.passionwoman.data.filters.FilterExtractor

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class SeasonTest {
    @Mock
    private lateinit var databaseMock: PassionWomanDatabase
    @Mock
    private lateinit var productSeasonDaoMock: ProductSeasonDao

    private val filterModel = FilterExtractor.Season()

    private fun createSeason(id: Int) =
        ProductSeasonEntity(
            code = "season$id",
            uiName = "Season $id"
        )

    @Before
    fun before() = runTest {
        whenever(databaseMock.productSeasonDao).doReturn(productSeasonDaoMock)
        whenever(productSeasonDaoMock.getAll()).doReturn(emptyList())
    }

    @Test
    fun extractAsJson_returnsBaseValues() = runTest {
        val result = filterModel.extractAsJson(databaseMock)

        assertEquals("multi", result.getString("type"))
        assertEquals("season", result.getString("code"))
        assertEquals("Season", result.getString("uiName"))
        assertEquals(4, result.getInt("priority"))
    }

    @Test
    fun extractAsJson_callsProductSeasonDao() = runTest {
        filterModel.extractAsJson(databaseMock)

        verify(productSeasonDaoMock, times(1)).getAll()
    }

    @Test
    fun extractAsJson_returnsEmptyValues_whenDaoIsEmpty() = runTest {
        val result = filterModel.extractAsJson(databaseMock)

        assertEquals("[]", result.getJSONArray("values").toString())
    }

    @Test
    fun extractAsJson_returnsCategories_whenDaoIsNotEmpty() = runTest {
        val season = createSeason(1)
        whenever(productSeasonDaoMock.getAll()).doReturn(listOf(season))
        val expectedValuesJson = """
            [
                {
                    "code": "${season.code}",
                    "uiName": "${season.uiName}"
                }
            ]
        """.let(::JSONArray)

        val result = filterModel.extractAsJson(databaseMock)

        assertEquals(expectedValuesJson.toString(), result.getJSONArray("values").toString())
    }
}
