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
import ru.babaetskv.passionwoman.data.database.dao.ProductCountryDao
import ru.babaetskv.passionwoman.data.database.entity.ProductCountryEntity
import java.io.IOException

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class ProductCountryDaoInstrumentedTest {
    private lateinit var database: PassionWomanDatabase
    private lateinit var productCountryDao: ProductCountryDao

    @Before
    fun before() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, PassionWomanDatabase::class.java)
            .build()
        productCountryDao = database.productCountryDao
    }

    @Test
    @Throws(Exception::class)
    fun getAll_returnsEmpty_whenThereAreNoCountries() = runTest {
        val result = productCountryDao.getAll()

        assertTrue(result.isEmpty())
    }

    @Test
    @Throws(Exception::class)
    fun getAll_returnsCountries_whenThereAreCountries() = runTest {
        val country = ProductCountryEntity(
            code = "country1",
            uiName = "Country 1"
        )
        productCountryDao.insert(country)

        val result = productCountryDao.getAll()

        assertEquals(listOf(country), result)
    }

    @Test
    @Throws(Exception::class)
    fun getAll_returnsSortedByNameCountries_whenThereAreCountries() = runTest {
        val countries = listOf(
            ProductCountryEntity(
                code = "country1",
                uiName = "Country 2"
            ),
            ProductCountryEntity(
                code = "country2",
                uiName = "Country 3"
            ),
            ProductCountryEntity(
                code = "country3",
                uiName = "Country 1"
            )
        )
        countries.forEach {
            productCountryDao.insert(it)
        }

        val result = productCountryDao.getAll()

        assertEquals(countries.sortedBy(ProductCountryEntity::uiName), result)
    }

    @Test
    @Throws(Exception::class)
    fun getAllCodesForProduct_returnsEmpty_whenThereAreNoCountries() = runTest {
        val result = productCountryDao.getAllCodesForProduct(1)

        assertTrue(result.isEmpty())
    }

    @Test
    @Throws(Exception::class)
    fun getAllCodesForProduct_returnsCodes_whenThereAreCountries() = runTest {
        val country = ProductCountryEntity(
            code = "country1",
            uiName = "Country 1"
        )
        productCountryDao.insert(country)

        val result = productCountryDao.getAllCodesForProduct(1)

        assertEquals(listOf(country.code), result)
    }

    @After
    @Throws(IOException::class)
    fun after() {
        database.close()
    }
}
