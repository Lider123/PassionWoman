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
import ru.babaetskv.passionwoman.data.database.dao.ProductMaterialDao
import ru.babaetskv.passionwoman.data.database.entity.ProductMaterialEntity
import ru.babaetskv.passionwoman.data.filters.FilterModel

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MaterialTest {
    @Mock
    lateinit var databaseMock: PassionWomanDatabase
    @Mock
    lateinit var productMaterialDaoMock: ProductMaterialDao

    private val filterModel = FilterModel.Material

    @Before
    fun before() = runTest {
        whenever(databaseMock.productMaterialDao).doReturn(productMaterialDaoMock)
        whenever(productMaterialDaoMock.getAll()).doReturn(emptyList())
    }

    @Test
    fun toJson_callsProductMaterialDao() = runTest {
        filterModel.toJson(databaseMock)

        verify(productMaterialDaoMock, times(1)).getAll()
    }

    @Test
    fun toJson_returnsBaseValues() = runTest {
        val result = filterModel.toJson(databaseMock)

        Assert.assertEquals("multi", result.getString("type"))
        Assert.assertEquals("material", result.getString("code"))
        Assert.assertEquals("Material", result.getString("uiName"))
        Assert.assertEquals(4, result.getInt("priority"))
    }

    @Test
    fun toJson_returnsEmptyValues_whenDaoIsEmpty() = runTest {
        val result = filterModel.toJson(databaseMock)

        Assert.assertEquals("[]", result.getJSONArray("values").toString())
    }

    @Test
    fun toJson_returnsCategories_whenDaoIsNotEmpty() = runTest {
        val materialEntity = ProductMaterialEntity(
            code = "material1",
            uiName = "Material 1"
        )
        whenever(productMaterialDaoMock.getAll()).doReturn(listOf(materialEntity))
        val expectedValuesJson = """
            [
                {
                    "code": "material1",
                    "uiName": "Material 1"
                }
            ]
        """.let(::JSONArray)

        val result = filterModel.toJson(databaseMock)

        Assert.assertEquals(expectedValuesJson.toString(), result.getJSONArray("values").toString())
    }
}
