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
import ru.babaetskv.passionwoman.data.database.dao.ProductSeasonDao
import ru.babaetskv.passionwoman.data.database.entity.ProductSeasonEntity
import ru.babaetskv.passionwoman.data.filters.FilterModel

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class SeasonTest {
    @Mock
    lateinit var databaseMock: PassionWomanDatabase
    @Mock
    lateinit var productSeasonDaoMock: ProductSeasonDao

    private val filterModel = FilterModel.Season

    @Before
    fun before() = runTest {
        whenever(databaseMock.productSeasonDao).doReturn(productSeasonDaoMock)
        whenever(productSeasonDaoMock.getAll()).doReturn(emptyList())
    }

    @Test
    fun toJson_returnsBaseValues() = runTest {
        val result = filterModel.toJson(databaseMock)

        Assert.assertEquals("multi", result.getString("type"))
        Assert.assertEquals("season", result.getString("code"))
        Assert.assertEquals("Season", result.getString("uiName"))
        Assert.assertEquals(4, result.getInt("priority"))
    }

    @Test
    fun toJson_callsProductSeasonDao() = runTest {
        filterModel.toJson(databaseMock)

        verify(productSeasonDaoMock, times(1)).getAll()
    }

    @Test
    fun toJson_returnsEmptyValues_whenDaoIsEmpty() = runTest {
        val result = filterModel.toJson(databaseMock)

        Assert.assertEquals("[]", result.getJSONArray("values").toString())
    }

    @Test
    fun toJson_returnsCategories_whenDaoIsNotEmpty() = runTest {
        val seasonEntity = ProductSeasonEntity(
            code = "season1",
            uiName = "Season 1"
        )
        whenever(productSeasonDaoMock.getAll()).doReturn(listOf(seasonEntity))
        val expectedValuesJson = """
            [
                {
                    "code": "season1",
                    "uiName": "Season 1"
                }
            ]
        """.let(::JSONArray)

        val result = filterModel.toJson(databaseMock)

        Assert.assertEquals(expectedValuesJson.toString(), result.getJSONArray("values").toString())
    }
}
