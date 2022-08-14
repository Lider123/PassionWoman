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
import ru.babaetskv.passionwoman.data.database.dao.CountryToProductDao
import ru.babaetskv.passionwoman.data.database.dao.ProductCountryDao
import ru.babaetskv.passionwoman.data.database.dao.ProductDao
import ru.babaetskv.passionwoman.data.database.entity.CategoryEntity
import ru.babaetskv.passionwoman.data.database.entity.CountryToProductEntity
import ru.babaetskv.passionwoman.data.database.entity.ProductCountryEntity
import ru.babaetskv.passionwoman.data.database.entity.ProductEntity
import java.io.IOException

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class ProductCountryDaoInstrumentedTest {
    private lateinit var database: PassionWomanDatabase
    private lateinit var productCountryDao: ProductCountryDao
    private lateinit var categoryDao: CategoryDao
    private lateinit var productDao: ProductDao
    private lateinit var countryToProductDao: CountryToProductDao

    private fun createProduct(
        id: Int,
        categoryId: Int,
        price: Float = 1f,
        priceWithDiscount: Float = 1f
    ) =
        ProductEntity(
            id = id,
            categoryId = categoryId,
            brandId = null,
            description = null,
            name = "Product $id",
            previewPath = "product_${id}_preview_path",
            price = price,
            priceWithDiscount = priceWithDiscount,
            rating = 0f
        )

    private fun createCategory(id: Int) =
        CategoryEntity(
            id = id,
            imagePath = "category_${id}_image_path",
            name = "Category $id"
        )

    private fun createCountry(id: Int) =
        ProductCountryEntity(
            code = "country$id",
            uiName = "Country $id"
        )

    @Before
    fun before() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, PassionWomanDatabase::class.java)
            .build()
        productCountryDao = database.productCountryDao
        countryToProductDao = database.countryToProductDao
        productDao = database.productDao
        categoryDao = database.categoryDao
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
        val category = createCategory(1)
        val products = listOf(
            createProduct(1, category.id),
            createProduct(2, category.id)
        )
        val countries = listOf(
            createCountry(1),
            createCountry(2),
            createCountry(3)
        )
        categoryDao.insert(category)
        products.forEach {
            productDao.insert(it)
        }
        countries.forEach {
            productCountryDao.insert(it)
        }
        listOf(
            CountryToProductEntity(
                id = 1,
                countryCode = "country1",
                productId = 1
            ),
            CountryToProductEntity(
                id = 2,
                countryCode = "country2",
                productId = 2
            ),
            CountryToProductEntity(
                id = 3,
                countryCode = "country3",
                productId = 1
            )
        ).forEach {
            countryToProductDao.insert(it)
        }

        val result = productCountryDao.getAllCodesForProduct(1)

        assertEquals(listOf("country1", "country3"), result)
    }

    @After
    @Throws(IOException::class)
    fun after() {
        database.close()
    }
}
