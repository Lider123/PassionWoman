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
import ru.babaets.passionwoman.data.database.dao.base.DaoInstrumentedTest
import ru.babaetskv.passionwoman.data.database.PassionWomanDatabase
import ru.babaetskv.passionwoman.data.database.dao.ColorDao
import java.io.IOException

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class ColorDaoInstrumentedTest : DaoInstrumentedTest() {
    private lateinit var database: PassionWomanDatabase
    private lateinit var colorDao: ColorDao

    @Before
    fun before() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, PassionWomanDatabase::class.java)
            .build()
        colorDao = database.colorDao
    }

    @Test
    @Throws(Exception::class)
    fun getAll_returnsEmpty_whenThereAreNoColors() = runTest {
        val result = colorDao.getAll()

        assertTrue(result.isEmpty())
    }

    @Test
    @Throws(Exception::class)
    fun getAll_returnsColors_whenThereAreColors() = runTest {
        val color = createColor(1)
        colorDao.insert(color)

        val result = colorDao.getAll()

        assertEquals(listOf(color), result)
    }

    @Test
    @Throws(Exception::class)
    fun getById_returnsNull_whenThereAreNoColors() = runTest {
        val result = colorDao.getById(1)

        assertNull(result)
    }

    @Test
    @Throws(Exception::class)
    fun getById_returnsNull_whenThereIsNoColorWithRequiredId() = runTest {
        colorDao.insert(createColor(1))

        val result = colorDao.getById(2)

        assertNull(result)
    }

    @Test
    @Throws(Exception::class)
    fun getById_returnsColor_whenThereIsColorWithRequiredId() = runTest {
        val color = createColor(1)
        colorDao.insert(color)

        val result = colorDao.getById(1)

        assertEquals(color, result)
    }

    @After
    @Throws(IOException::class)
    fun after() {
        database.close()
    }
}
