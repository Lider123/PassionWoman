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
import ru.babaetskv.passionwoman.data.database.dao.ProductSizeDao
import ru.babaetskv.passionwoman.data.database.entity.ProductSizeEntity
import ru.babaetskv.passionwoman.data.filters.FilterExtractor

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class SizeTest {
    @Mock
    private lateinit var databaseMock: PassionWomanDatabase
    @Mock
    private lateinit var productSizeDaoMock: ProductSizeDao

    private val filterModel = FilterExtractor.Size()

    private fun createSize(id: Int) =
        ProductSizeEntity(
            code = "size$id",
            uiName = "Size $id"
        )

    @Before
    fun before() = runTest {
        whenever(databaseMock.productSizeDao).doReturn(productSizeDaoMock)
        whenever(productSizeDaoMock.getAll()).doReturn(emptyList())
    }

    @Test
    fun extractAsJson_returnsBaseValues() = runTest {
        val result = filterModel.extractAsJson(databaseMock)

        assertEquals("multi", result.getString("type"))
        assertEquals("sizes", result.getString("code"))
        assertEquals("Sizes", result.getString("uiName"))
        assertEquals(4, result.getInt("priority"))
    }

    @Test
    fun extractAsJson_callsProductSizeDao() = runTest {
        filterModel.extractAsJson(databaseMock)

        verify(productSizeDaoMock, times(1)).getAll()
    }

    @Test
    fun extractAsJson_returnsEmptyValues_whenDaoIsEmpty() = runTest {
        val result = filterModel.extractAsJson(databaseMock)

        assertEquals("[]", result.getJSONArray("values").toString())
    }

    @Test
    fun extractAsJson_returnsCategories_whenDaoIsNotEmpty() = runTest {
        val size = createSize(1)
        whenever(productSizeDaoMock.getAll()).doReturn(listOf(size))
        val expectedValuesJson = """
            [
                {
                    "code": "${size.code}",
                    "uiName": "${size.uiName}"
                }
            ]
        """.let(::JSONArray)

        val result = filterModel.extractAsJson(databaseMock)

        assertEquals(expectedValuesJson.toString(), result.getJSONArray("values").toString())
    }
}
