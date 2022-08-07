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
import ru.babaetskv.passionwoman.data.database.dao.ColorDao
import ru.babaetskv.passionwoman.data.database.entity.ColorEntity
import ru.babaetskv.passionwoman.data.filters.FilterModel

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class ColorTest {
    @Mock
    lateinit var databaseMock: PassionWomanDatabase
    @Mock
    lateinit var colorDaoMock: ColorDao

    private val filterModel = FilterModel.Color

    @Before
    fun before() = runTest {
        whenever(databaseMock.colorDao).doReturn(colorDaoMock)
        whenever(colorDaoMock.getAll()).doReturn(emptyList())
    }

    @Test
    fun toJson_returnsBaseValues() = runTest {
        val result = filterModel.toJson(databaseMock)

        Assert.assertEquals("color", result.getString("type"))
        Assert.assertEquals("color", result.getString("code"))
        Assert.assertEquals("Color", result.getString("uiName"))
        Assert.assertEquals(3, result.getInt("priority"))
    }

    @Test
    fun toJson_callsColorDao() = runTest {
        filterModel.toJson(databaseMock)

        verify(colorDaoMock, times(1)).getAll()
    }

    @Test
    fun toJson_returnsEmptyValues_whenDaoIsEmpty() = runTest {
        val result = filterModel.toJson(databaseMock)

        Assert.assertEquals("[]", result.getJSONArray("values").toString())
    }

    @Test
    fun toJson_returnsCategories_whenDaoIsNotEmpty() = runTest {
        val colorEntity = ColorEntity(
            id = 1,
            uiName = "color 1",
            hex = "hex1"
        )
        whenever(colorDaoMock.getAll()).doReturn(listOf(colorEntity))
        val expectedValuesJson = """
            [
                {
                    "id": 1,
                    "uiName": "color 1",
                    "hex": "hex1"
                }
            ]
        """.let(::JSONArray)

        val result = filterModel.toJson(databaseMock)

        Assert.assertEquals(expectedValuesJson.toString(), result.getJSONArray("values").toString())
    }
}
