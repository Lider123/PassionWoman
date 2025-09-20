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
import ru.babaetskv.passionwoman.data.database.dao.ProductMaterialDao
import ru.babaetskv.passionwoman.data.database.entity.ProductMaterialEntity
import ru.babaetskv.passionwoman.data.filters.FilterExtractor

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MaterialTest {
    @Mock
    private lateinit var databaseMock: PassionWomanDatabase
    @Mock
    private lateinit var productMaterialDaoMock: ProductMaterialDao

    private val filterModel = FilterExtractor.Material()

    private fun createMaterial(id: Int) =
        ProductMaterialEntity(
            code = "material$id",
            uiName = "Material $id"
        )

    @Before
    fun before() = runTest {
        whenever(databaseMock.productMaterialDao).doReturn(productMaterialDaoMock)
        whenever(productMaterialDaoMock.getAll()).doReturn(emptyList())
    }

    @Test
    fun extractAsJson_callsProductMaterialDao() = runTest {
        filterModel.extractAsJson(databaseMock)

        verify(productMaterialDaoMock, times(1)).getAll()
    }

    @Test
    fun extractAsJson_returnsBaseValues() = runTest {
        val result = filterModel.extractAsJson(databaseMock)

        assertEquals("multi", result.getString("type"))
        assertEquals("material", result.getString("code"))
        assertEquals("Material", result.getString("uiName"))
        assertEquals(4, result.getInt("priority"))
    }

    @Test
    fun extractAsJson_returnsEmptyValues_whenDaoIsEmpty() = runTest {
        val result = filterModel.extractAsJson(databaseMock)

        assertEquals("[]", result.getJSONArray("values").toString())
    }

    @Test
    fun extractAsJson_returnsCategories_whenDaoIsNotEmpty() = runTest {
        val material = createMaterial(1)
        whenever(productMaterialDaoMock.getAll()).doReturn(listOf(material))
        val expectedValuesJson = """
            [
                {
                    "code": "${material.code}",
                    "uiName": "${material.uiName}"
                }
            ]
        """.let(::JSONArray)

        val result = filterModel.extractAsJson(databaseMock)

        assertEquals(expectedValuesJson.toString(), result.getJSONArray("values").toString())
    }
}
