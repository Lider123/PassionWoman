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
import ru.babaetskv.passionwoman.data.database.dao.CategoryDao
import ru.babaetskv.passionwoman.data.database.entity.CategoryEntity
import ru.babaetskv.passionwoman.data.filters.FilterExtractor

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class CategoryTest {
    @Mock
    private lateinit var databaseMock: PassionWomanDatabase
    @Mock
    private lateinit var categoryDaoMock: CategoryDao

    private val filterModel = FilterExtractor.Category()

    private fun createCategory(id: Int) =
        CategoryEntity(
            id = id,
            name = "Category $id",
            imagePath = "category_${id}_image_path"
        )

    @Before
    fun before() = runTest {
        whenever(databaseMock.categoryDao).doReturn(categoryDaoMock)
        whenever(categoryDaoMock.getAll()).doReturn(emptyList())
    }

    @Test
    fun extractAsJson_returnsBaseValues() = runTest {
        val result = filterModel.extractAsJson(databaseMock)

        assertEquals("multi", result.getString("type"))
        assertEquals("category", result.getString("code"))
        assertEquals("Categories", result.getString("uiName"))
        assertEquals(5, result.getInt("priority"))
    }

    @Test
    fun extractAsJson_callsCategoryDao() = runTest {
        filterModel.extractAsJson(databaseMock)

        verify(categoryDaoMock, times(1)).getAll()
    }

    @Test
    fun extractAsJson_returnsEmptyValues_whenDaoIsEmpty() = runTest {
        val result = filterModel.extractAsJson(databaseMock)

        assertEquals("[]", result.getJSONArray("values").toString())
    }

    @Test
    fun extractAsJson_returnsCategories_whenDaoIsNotEmpty() = runTest {
        val categoryEntity = createCategory(1)
        whenever(categoryDaoMock.getAll()).doReturn(listOf(categoryEntity))
        val expectedValuesJson = """
            [
                {
                    "id": ${categoryEntity.id},
                    "uiName": "${categoryEntity.name}"
                }
            ]
        """.let(::JSONArray)

        val result = filterModel.extractAsJson(databaseMock)

        assertEquals(expectedValuesJson.toString(), result.getJSONArray("values").toString())
    }
}
