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
import ru.babaetskv.passionwoman.data.database.dao.*
import java.io.IOException

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class ProductSeasonDaoInstrumentedTest : DaoInstrumentedTest() {
    private lateinit var database: PassionWomanDatabase
    private lateinit var productSeasonDao: ProductSeasonDao
    private lateinit var productDao: ProductDao
    private lateinit var categoryDao: CategoryDao
    private lateinit var seasonToProductDao: SeasonToProductDao

    @Before
    fun before() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, PassionWomanDatabase::class.java)
            .build()
        productSeasonDao = database.productSeasonDao
        productDao = database.productDao
        categoryDao = database.categoryDao
        seasonToProductDao = database.seasonToProductDao
    }

    @Test
    @Throws(Exception::class)
    fun getAll_returnsEmpty_whenThereAreNoSeasons() = runTest {
        val result = productSeasonDao.getAll()

        assertTrue(result.isEmpty())
    }

    @Test
    @Throws(Exception::class)
    fun getAll_returnsSeasons_whenThereAreSeasons() = runTest {
        val seasons = listOf(
            createProductSeason(1),
            createProductSeason(2)
        )
        productSeasonDao.insert(*seasons.toTypedArray())

        val result = productSeasonDao.getAll()

        assertEquals(seasons, result)
    }

    @Test
    @Throws(Exception::class)
    fun getCodesForProduct_returnsEmpty_whenThereAreNoSeasons() = runTest {
        val result = productSeasonDao.getCodesForProduct(1)

        assertTrue(result.isEmpty())
    }

    @Test
    @Throws(Exception::class)
    fun getCodesForProduct_returnsEmpty_whenThereAreNoProducts() = runTest {
        val seasons = listOf(
            createProductSeason(1),
            createProductSeason(2)
        )
        productSeasonDao.insert(*seasons.toTypedArray())

        val result = productSeasonDao.getCodesForProduct(1)

        assertTrue(result.isEmpty())
    }

    @Test
    @Throws(Exception::class)
    fun getCodesForProduct_returnsEmpty_whenThereAreNoSeasonToProductEntities() = runTest {
        val seasons = listOf(
            createProductSeason(1),
            createProductSeason(2)
        )
        val category = createCategory(1)
        val product = createProduct(1, category.id)
        productSeasonDao.insert(*seasons.toTypedArray())
        categoryDao.insert(category)
        productDao.insert(product)

        val result = productSeasonDao.getCodesForProduct(1)

        assertTrue(result.isEmpty())
    }

    @Test
    @Throws(Exception::class)
    fun getCodesForProduct_returnsEmpty_whenThereAreNoSeasonsForRequiredProductId() = runTest {
        val seasons = listOf(
            createProductSeason(1),
            createProductSeason(2)
        )
        val category = createCategory(1)
        val products = listOf(
            createProduct(1, category.id),
            createProduct(2, category.id)
        )
        val seasonToProductEntities = listOf(
            createSeasonToProductEntity(1, 1, 1),
            createSeasonToProductEntity(2, 2, 1)
        )
        productSeasonDao.insert(*seasons.toTypedArray())
        categoryDao.insert(category)
        productDao.insert(*products.toTypedArray())
        seasonToProductDao.insert(*seasonToProductEntities.toTypedArray())

        val result = productSeasonDao.getCodesForProduct(2)

        assertTrue(result.isEmpty())
    }

    @Test
    @Throws(Exception::class)
    fun getCodesForProduct_returnsSeasonsForRequiredProductId_whenThereAreSeasonsForRequiredProductId() = runTest {
        val seasons = listOf(
            createProductSeason(1),
            createProductSeason(2),
            createProductSeason(3),
            createProductSeason(4)
        )
        val category = createCategory(1)
        val products = listOf(
            createProduct(1, category.id),
            createProduct(2, category.id)
        )
        val seasonToProductEntities = listOf(
            createSeasonToProductEntity(1, 1, 1),
            createSeasonToProductEntity(2, 2, 1),
            createSeasonToProductEntity(3, 3, 2),
            createSeasonToProductEntity(4, 4, 2)
        )
        productSeasonDao.insert(*seasons.toTypedArray())
        categoryDao.insert(category)
        productDao.insert(*products.toTypedArray())
        seasonToProductDao.insert(*seasonToProductEntities.toTypedArray())

        val result = productSeasonDao.getCodesForProduct(2)

        val expected = listOf("season3", "season4")
        assertEquals(expected, result)
    }

    @After
    @Throws(IOException::class)
    fun after() {
        database.close()
    }
}
