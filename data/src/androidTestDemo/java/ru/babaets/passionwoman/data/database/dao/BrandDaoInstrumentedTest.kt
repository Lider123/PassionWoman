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
import ru.babaetskv.passionwoman.data.database.dao.BrandDao
import java.io.IOException

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class BrandDaoInstrumentedTest : DaoInstrumentedTest() {
    private lateinit var database: PassionWomanDatabase
    private lateinit var brandDao: BrandDao

    @Before
    fun before() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, PassionWomanDatabase::class.java)
            .build()
        brandDao = database.brandDao
    }

    @Test
    @Throws(Exception::class)
    fun getAll_returnsEmpty_whenThereAreNoBrands() = runTest {
        val result = brandDao.getAll()

        assertTrue(result.isEmpty())
    }

    @Test
    @Throws(Exception::class)
    fun getAll_returnsBrands_whenThereAreBrands() = runTest {
        val brandEntity = createBrand(1)
        brandDao.insert(brandEntity)

        val result = brandDao.getAll()

        assertEquals(listOf(brandEntity), result)
    }

    @Test
    @Throws(Exception::class)
    fun getAll_returnsSortedBrandsByName_whenThereAreBrands() = runTest {
        val brandEntities = listOf(
            createBrand(3),
            createBrand(1),
            createBrand(2)
        )
        brandEntities.forEach {
            brandDao.insert(it)
        }

        val result = brandDao.getAll()

        assertEquals(brandEntities.sortedBy { it.name }, result)
    }

    @Test
    @Throws(Exception::class)
    fun getPopular_returnsEmpty_whenThereAreNoBrandsAndCountIsGreaterThanZero() = runTest {
        val result = brandDao.getPopular(2)

        assertTrue(result.isEmpty())
    }

    @Test
    @Throws(Exception::class)
    fun getPopular_returnsAll_whenThereAreLessBrandsThanCount() = runTest {
        val brandEntities = listOf(
            createBrand(1),
            createBrand(2)
        )
        brandEntities.forEach {
            brandDao.insert(it)
        }

        val result = brandDao.getPopular(4)

        assertEquals(brandEntities, result)
    }

    @Test
    @Throws(Exception::class)
    fun getPopular_returnsListOfSizeCount_whenThereAreMoreBrandsThanCount() = runTest {
        val brandEntities = listOf(
            createBrand(1),
            createBrand(2)
        )
        brandEntities.forEach {
            brandDao.insert(it)
        }

        val result = brandDao.getPopular(1)

        assertEquals(brandEntities.take(1), result)
    }

    @Test
    @Throws(Exception::class)
    fun getPopular_returnsSortedBrandsByOccurrencesDescInProducts_whenThereAreBrands() = runTest {
        listOf(
            createBrand(1),
            createBrand(2),
            createBrand(3)
        ).forEach {
            brandDao.insert(it)
        }
        database.categoryDao.insert(createCategory(1))
        listOf(
            createProduct(1, 1, brandId = 1),
            createProduct(2, 1, brandId = 2),
            createProduct(3, 1, brandId = 2),
            createProduct(4, 1, brandId = 2),
            createProduct(5, 1, brandId = 3),
            createProduct(6, 1, brandId = 3),
        ).forEach {
            database.productDao.insert(it)
        }
        val expectedResult = listOf(
            createBrand(2),
            createBrand(3),
            createBrand(1),
        )

        val result = brandDao.getPopular(3)

        assertEquals(expectedResult, result)
    }

    @Test
    @Throws(Exception::class)
    fun getById_returnsNull_whenThereAreNoBrands() = runTest {
        val result = brandDao.getById(1)

        assertNull(result)
    }

    @Test
    @Throws(Exception::class)
    fun getById_returnsNull_whenThereIsNoBrandWithRequiredId() = runTest {
        brandDao.insert(createBrand(1))
        val result = brandDao.getById(2)

        assertNull(result)
    }

    @Test
    @Throws(Exception::class)
    fun getById_returnsBrand_whenThereIsBrandWithRequiredId() = runTest {
        val brand = createBrand(1)
        brandDao.insert(brand)

        val result = brandDao.getById(1)

        assertEquals(brand, result)
    }

    @After
    @Throws(IOException::class)
    fun after() {
        database.close()
    }
}
