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
import ru.babaetskv.passionwoman.data.database.dao.ProductStyleDao
import ru.babaetskv.passionwoman.data.database.entity.ProductStyleEntity
import ru.babaetskv.passionwoman.data.filters.FilterModel

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class StyleTest {
    @Mock
    lateinit var databaseMock: PassionWomanDatabase
    @Mock
    lateinit var productStyleDaoMock: ProductStyleDao

    private val filterModel = FilterModel.Style

    @Before
    fun before() = runTest {
        whenever(databaseMock.productStyleDao).doReturn(productStyleDaoMock)
        whenever(productStyleDaoMock.getAll()).doReturn(emptyList())
    }

    @Test
    fun toJson_returnsBaseValues() = runTest {
        val result = filterModel.toJson(databaseMock)

        Assert.assertEquals("multi", result.getString("type"))
        Assert.assertEquals("style", result.getString("code"))
        Assert.assertEquals("Style", result.getString("uiName"))
        Assert.assertEquals(4, result.getInt("priority"))
    }

    @Test
    fun toJson_callsProductStyleDao() = runTest {
        filterModel.toJson(databaseMock)

        verify(productStyleDaoMock, times(1)).getAll()
    }

    @Test
    fun toJson_returnsEmptyValues_whenDaoIsEmpty() = runTest {
        val result = filterModel.toJson(databaseMock)

        Assert.assertEquals("[]", result.getJSONArray("values").toString())
    }

    @Test
    fun toJson_returnsCategories_whenDaoIsNotEmpty() = runTest {
        val styleEntity = ProductStyleEntity(
            code = "style1",
            uiName = "Style 1"
        )
        whenever(productStyleDaoMock.getAll()).doReturn(listOf(styleEntity))
        val expectedValuesJson = """
            [
                {
                    "code": "style1",
                    "uiName": "Style 1"
                }
            ]
        """.let(::JSONArray)

        val result = filterModel.toJson(databaseMock)

        Assert.assertEquals(expectedValuesJson.toString(), result.getJSONArray("values").toString())
    }
}
