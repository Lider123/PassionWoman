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
import ru.babaetskv.passionwoman.data.database.dao.ProductStyleDao
import ru.babaetskv.passionwoman.data.database.entity.ProductStyleEntity
import ru.babaetskv.passionwoman.data.filters.FilterExtractor

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class StyleTest {
    @Mock
    private lateinit var databaseMock: PassionWomanDatabase
    @Mock
    private lateinit var productStyleDaoMock: ProductStyleDao

    private val filterModel = FilterExtractor.Style()

    private fun createStyle(id: Int) =
        ProductStyleEntity(
            code = "style$id",
            uiName = "Style $id"
        )

    @Before
    fun before() = runTest {
        whenever(databaseMock.productStyleDao).doReturn(productStyleDaoMock)
        whenever(productStyleDaoMock.getAll()).doReturn(emptyList())
    }

    @Test
    fun extractAsJson_returnsBaseValues() = runTest {
        val result = filterModel.extractAsJson(databaseMock)

        assertEquals("multi", result.getString("type"))
        assertEquals("style", result.getString("code"))
        assertEquals("Style", result.getString("uiName"))
        assertEquals(4, result.getInt("priority"))
    }

    @Test
    fun extractAsJson_callsProductStyleDao() = runTest {
        filterModel.extractAsJson(databaseMock)

        verify(productStyleDaoMock, times(1)).getAll()
    }

    @Test
    fun extractAsJson_returnsEmptyValues_whenDaoIsEmpty() = runTest {
        val result = filterModel.extractAsJson(databaseMock)

        assertEquals("[]", result.getJSONArray("values").toString())
    }

    @Test
    fun extractAsJson_returnsCategories_whenDaoIsNotEmpty() = runTest {
        val style = createStyle(1)
        whenever(productStyleDaoMock.getAll()).doReturn(listOf(style))
        val expectedValuesJson = """
            [
                {
                    "code": "${style.code}",
                    "uiName": "${style.uiName}"
                }
            ]
        """.let(::JSONArray)

        val result = filterModel.extractAsJson(databaseMock)

        assertEquals(expectedValuesJson.toString(), result.getJSONArray("values").toString())
    }
}
