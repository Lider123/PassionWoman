package ru.babaetskv.passionwoman.data.filters.filtermodel

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
import ru.babaetskv.passionwoman.data.database.dao.CategoryDao
import ru.babaetskv.passionwoman.data.database.entity.CategoryEntity
import ru.babaetskv.passionwoman.data.filters.FilterModel

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class CategoryTest {
    @Mock
    lateinit var databaseMock: PassionWomanDatabase
    @Mock
    lateinit var categoryDaoMock: CategoryDao

    private val filterModel = FilterModel.Category

    @Before
    fun before() = runTest {
        whenever(databaseMock.categoryDao).doReturn(categoryDaoMock)
        whenever(categoryDaoMock.getAll()).doReturn(emptyList())
    }

    @Test
    fun toJson_returnsBaseValues() = runTest {
        val result = filterModel.toJson(databaseMock)

        assertEquals("multi", result.getString("type"))
        assertEquals("category", result.getString("code"))
        assertEquals("Categories", result.getString("uiName"))
        assertEquals(5, result.getInt("priority"))
    }

    @Test
    fun toJson_callsCategoryDao() = runTest {
        filterModel.toJson(databaseMock)

        verify(categoryDaoMock, times(1)).getAll()
    }

    @Test
    fun toJson_returnsEmptyValues_whenDaoIsEmpty() = runTest {
        val result = filterModel.toJson(databaseMock)

        assertEquals("[]", result.getJSONArray("values").toString())
    }

    @Test
    fun toJson_returnsCategories_whenDaoIsNotEmpty() = runTest {
        val categoryEntity = CategoryEntity(
            id = 1,
            name = "Category1",
            imagePath = "category_1_image"
        )
        whenever(categoryDaoMock.getAll()).doReturn(listOf(categoryEntity))
        val expectedValuesJson = """
            [
                {
                    "id": 1,
                    "uiName": "Category1"
                }
            ]
        """.let(::JSONArray)

        val result = filterModel.toJson(databaseMock)

        assertEquals(expectedValuesJson.toString(), result.getJSONArray("values").toString())
    }
}
