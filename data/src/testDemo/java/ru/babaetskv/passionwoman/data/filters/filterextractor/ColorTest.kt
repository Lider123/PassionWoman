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
import ru.babaetskv.passionwoman.data.database.dao.ColorDao
import ru.babaetskv.passionwoman.data.database.entity.ColorEntity
import ru.babaetskv.passionwoman.data.filters.FilterExtractor

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class ColorTest {
    @Mock
    private lateinit var databaseMock: PassionWomanDatabase
    @Mock
    private lateinit var colorDaoMock: ColorDao

    private val filterModel = FilterExtractor.Color()

    private fun createColor(id: Int) =
        ColorEntity(
            id = 1,
            uiName = "color $id",
            hex = "hex$id"
        )

    @Before
    fun before() = runTest {
        whenever(databaseMock.colorDao).doReturn(colorDaoMock)
        whenever(colorDaoMock.getAll()).doReturn(emptyList())
    }

    @Test
    fun extractAsJson_returnsBaseValues() = runTest {
        val result = filterModel.extractAsJson(databaseMock)

        assertEquals("color", result.getString("type"))
        assertEquals("color", result.getString("code"))
        assertEquals("Color", result.getString("uiName"))
        assertEquals(3, result.getInt("priority"))
    }

    @Test
    fun extractAsJson_callsColorDao() = runTest {
        filterModel.extractAsJson(databaseMock)

        verify(colorDaoMock, times(1)).getAll()
    }

    @Test
    fun extractAsJson_returnsEmptyValues_whenDaoIsEmpty() = runTest {
        val result = filterModel.extractAsJson(databaseMock)

        assertEquals("[]", result.getJSONArray("values").toString())
    }

    @Test
    fun extractAsJson_returnsCategories_whenDaoIsNotEmpty() = runTest {
        val color = createColor(1)
        whenever(colorDaoMock.getAll()).doReturn(listOf(color))
        val expectedValuesJson = """
            [
                {
                    "id": ${color.id},
                    "uiName": "${color.uiName}",
                    "hex": "${color.hex}"
                }
            ]
        """.let(::JSONArray)

        val result = filterModel.extractAsJson(databaseMock)

        assertEquals(expectedValuesJson.toString(), result.getJSONArray("values").toString())
    }
}
