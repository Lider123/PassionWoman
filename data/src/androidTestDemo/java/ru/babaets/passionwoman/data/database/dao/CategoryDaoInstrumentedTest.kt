package ru.babaets.passionwoman.data.database.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import ru.babaetskv.passionwoman.data.database.PassionWomanDatabase
import ru.babaetskv.passionwoman.data.database.dao.CategoryDao
import ru.babaetskv.passionwoman.data.database.entity.CategoryEntity
import java.io.IOException

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class CategoryDaoInstrumentedTest {
    private lateinit var database: PassionWomanDatabase
    private lateinit var categoryDao: CategoryDao

    private fun createCategory(id: Int) =
        CategoryEntity(
            id = id,
            name = "Category $id",
            imagePath = "category_${id}_image_path"
        )

    @Before
    fun before() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, PassionWomanDatabase::class.java)
            .build()
        categoryDao = database.categoryDao
    }

    @Test
    @Throws(Exception::class)
    fun getAll_returnsEmpty_whenThereAreNoCategories() = runTest {
        val result = categoryDao.getAll()

        assertTrue(result.isEmpty())
    }

    @Test
    @Throws(Exception::class)
    fun getAll_returnsCategories_whenThereAreCategories() = runTest {
        val category = createCategory(1)
        categoryDao.insert(category)

        val result = categoryDao.getAll()

        assertEquals(listOf(category), result)
    }

    @Test
    @Throws(Exception::class)
    fun getById_returnsNull_whenThereIsNoCategoryWithRequiredId() = runTest {
        val result = categoryDao.getById(1)

        assertNull(result)
    }

    @Test
    @Throws(Exception::class)
    fun getById_returnsCategoryWithRequiredId_whenThereIsCategoryWithRequiredId() = runTest {
        val category = createCategory(1)
        categoryDao.insert(category)

        val result = categoryDao.getById(1)

        assertEquals(category, result)
    }

    @After
    @Throws(IOException::class)
    fun after() {
        database.close()
    }
}
