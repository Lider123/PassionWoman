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
import ru.babaetskv.passionwoman.data.database.dao.BrandDao
import ru.babaetskv.passionwoman.data.database.entity.BrandEntity
import ru.babaetskv.passionwoman.data.filters.FilterModel

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class BrandTest {
    @Mock
    lateinit var databaseMock: PassionWomanDatabase
    @Mock
    lateinit var brandDaoMock: BrandDao

    private val filterModel = FilterModel.Brand

    @Before
    fun before() = runTest {
        whenever(databaseMock.brandDao).doReturn(brandDaoMock)
        whenever(brandDaoMock.getAll()).doReturn(emptyList())
    }

    @Test
    fun toJson_returnsBaseValues() = runTest {
        val result = filterModel.toJson(databaseMock)

        Assert.assertEquals("multi", result.getString("type"))
        Assert.assertEquals("brands", result.getString("code"))
        Assert.assertEquals("Brands", result.getString("uiName"))
        Assert.assertEquals(4, result.getInt("priority"))
    }

    @Test
    fun toJson_callsBrandDao() = runTest {
        filterModel.toJson(databaseMock)

        verify(brandDaoMock, times(1)).getAll()
    }

    @Test
    fun toJson_returnsEmptyValues_whenDaoIsEmpty() = runTest {
        val result = filterModel.toJson(databaseMock)

        Assert.assertEquals("[]", result.getJSONArray("values").toString())
    }

    @Test
    fun toJson_returnsCategories_whenDaoIsNotEmpty() = runTest {
        val brandEntity = BrandEntity(
            id = 1,
            name = "Brand 1",
            logoPath = "logo_path"
        )
        whenever(brandDaoMock.getAll()).doReturn(listOf(brandEntity))
        val expectedValuesJson = """
            [
                {
                    "id": 1,
                    "uiName": "Brand 1"
                }
            ]
        """.let(::JSONArray)

        val result = filterModel.toJson(databaseMock)

        Assert.assertEquals(expectedValuesJson.toString(), result.getJSONArray("values").toString())
    }
}
