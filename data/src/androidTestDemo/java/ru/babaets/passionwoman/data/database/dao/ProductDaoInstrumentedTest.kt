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
import ru.babaetskv.passionwoman.data.database.dao.CategoryDao
import ru.babaetskv.passionwoman.data.database.dao.ProductDao
import java.io.IOException

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class ProductDaoInstrumentedTest : DaoInstrumentedTest() {
    private lateinit var database: PassionWomanDatabase
    private lateinit var productDao: ProductDao
    private lateinit var categoryDao: CategoryDao

    @Before
    fun before() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, PassionWomanDatabase::class.java)
            .build()
        productDao = database.productDao
        categoryDao = database.categoryDao
    }

    @Test
    @Throws(Exception::class)
    fun getById_returnsNull_whenThereAreNoProducts() = runTest {
        val result = productDao.getById(1)

        assertNull(result)
    }

    @Test
    @Throws(Exception::class)
    fun getById_returnsNull_whenThereAreNoProductWithRequiredId() = runTest {
        val category = createCategory(1)
        val product = createProduct(1, category.id)
        categoryDao.insert(category)
        productDao.insert(product)

        val result = productDao.getById(2)

        assertNull(result)
    }

    @Test
    @Throws(Exception::class)
    fun getById_returnsProduct_whenThereIsProductWithRequiredId() = runTest {
        val category = createCategory(1)
        val product = createProduct(1, category.id)
        categoryDao.insert(category)
        productDao.insert(product)

        val result = productDao.getById(1)

        assertEquals(product, result)
    }

    @Test
    @Throws(Exception::class)
    fun getByCategoryId_returnsEmpty_whenThereAreNoProducts() = runTest {
        val result = productDao.getByCategoryId(1)

        assertTrue(result.isEmpty())
    }

    @Test
    @Throws(Exception::class)
    fun getByCategoryId_returnsEmpty_whenThereAreNoProductsOfRequiredCategory() = runTest {
        val category = createCategory(1)
        val product = createProduct(1, category.id)
        categoryDao.insert(category)
        productDao.insert(product)

        val result = productDao.getByCategoryId(2)

        assertTrue(result.isEmpty())
    }

    @Test
    @Throws(Exception::class)
    fun getByCategoryId_returnsProductsOfRequiredCategory_whenThereAreProductsOfRequiredCategory() =
        runTest {
            val categories = listOf(
                createCategory(1),
                createCategory(2)
            )
            val products = listOf(
                createProduct(1, 1),
                createProduct(2, 2)
            )
            categories.forEach {
                categoryDao.insert(it)
            }
            products.forEach {
                productDao.insert(it)
            }

            val result = productDao.getByCategoryId(1)

            assertEquals(products.filter { it.categoryId == 1L }, result)
        }

    @Test
    @Throws(Exception::class)
    fun getRandom_returnsEmpty_whenThereAreNoProducts() = runTest {
        val result = productDao.getRandom(1)

        assertTrue(result.isEmpty())
    }

    @Test
    @Throws(Exception::class)
    fun getRandom_returnsShuffled_whenThereAreProducts() = runTest {
        val category = createCategory(1)
        val products = listOf(
            createProduct(1, category.id),
            createProduct(2, category.id),
            createProduct(3, category.id),
            createProduct(4, category.id)
        )
        categoryDao.insert(category)
        products.forEach {
            productDao.insert(it)
        }

        val result = productDao.getRandom(4)

        assertNotEquals(products, result)
        assertEquals(products, result.sortedBy { it.id })
    }

    @Test
    @Throws(Exception::class)
    fun getRandom_returnsAll_whenLimitIsGreaterThanProductsCount() = runTest {
        val category = createCategory(1)
        val products = listOf(
            createProduct(1, category.id),
            createProduct(2, category.id),
        )
        categoryDao.insert(category)
        products.forEach {
            productDao.insert(it)
        }

        val result = productDao.getRandom(4)

        assertEquals(products.size, result.size)
    }

    @Test
    @Throws(Exception::class)
    fun getRandom_returnsLimited_whenLimitIsLessThanProductsCount() = runTest {
        val category = createCategory(1)
        val products = listOf(
            createProduct(1, category.id),
            createProduct(2, category.id),
            createProduct(3, category.id),
            createProduct(4, category.id),
        )
        categoryDao.insert(category)
        products.forEach {
            productDao.insert(it)
        }

        val result = productDao.getRandom(2)

        assertEquals(2, result.size)
    }

    @Test
    @Throws(Exception::class)
    fun getWithDiscount_returnsEmpty_whenThereAreNoProducts() = runTest {
        val result = productDao.getWithDiscount()

        assertTrue(result.isEmpty())
    }

    @Test
    @Throws(Exception::class)
    fun getWithDiscount_returnsEmpty_whenThereAreNoProductsWithDiscount() = runTest {
        val category = createCategory(1)
        val product = createProduct(1, 1)
        categoryDao.insert(category)
        productDao.insert(product)

        val result = productDao.getWithDiscount()

        assertTrue(result.isEmpty())
    }

    @Test
    @Throws(Exception::class)
    fun getWithDiscount_returnsProductsWithDiscount_whenThereAreProductsWithDiscount() = runTest {
        val category = createCategory(1)
        val products = listOf(
            createProduct(1, 1),
            createProduct(2, 1,
                price = 2f,
                priceWithDiscount = 1f
            )
        )
        categoryDao.insert(category)
        products.forEach {
            productDao.insert(it)
        }

        val result = productDao.getWithDiscount()

        assertEquals(products.filter { it.priceWithDiscount < it.price }, result)
    }

    @Test
    @Throws(Exception::class)
    fun getByIds_returnsEmpty_whenThereAreNoProducts() = runTest {
        val result = productDao.getByIds(listOf(1, 2))

        assertTrue(result.isEmpty())
    }

    @Test
    @Throws(Exception::class)
    fun getByIds_returnsAll_whenThereAllIdsAreRequired() = runTest {
        val category = createCategory(1)
        val products = listOf(
            createProduct(1, category.id),
            createProduct(2, category.id),
            createProduct(3, category.id)
        )
        categoryDao.insert(category)
        products.forEach {
            productDao.insert(it)
        }

        val result = productDao.getByIds(listOf(1, 2, 3))

        assertEquals(products, result)
    }

    @Test
    @Throws(Exception::class)
    fun getByIds_returnsSome_whenIdsIntersected() = runTest {
        val category = createCategory(1)
        val products = listOf(
            createProduct(1, category.id),
            createProduct(2, category.id),
            createProduct(3, category.id)
        )
        categoryDao.insert(category)
        products.forEach {
            productDao.insert(it)
        }

        val result = productDao.getByIds(listOf(2, 3, 4))

        assertEquals(products.filter { it.id in listOf(2L, 3L, 4L) }, result)
    }

    @Test
    @Throws(Exception::class)
    fun getByIds_returnsEmpty_whenThereIsNoIdsIntersection() = runTest {
        val category = createCategory(1)
        val products = listOf(
            createProduct(1, category.id),
            createProduct(2, category.id),
            createProduct(3, category.id)
        )
        categoryDao.insert(category)
        products.forEach {
            productDao.insert(it)
        }

        val result = productDao.getByIds(listOf(4, 5, 6))

        assertTrue(result.isEmpty())
    }

    @Test
    @Throws(Exception::class)
    fun getByIds_returnsAll_whenAllProductsInsideIdsList() = runTest {
        val category = createCategory(1)
        val products = listOf(
            createProduct(1, category.id),
            createProduct(2, category.id),
            createProduct(3, category.id)
        )
        categoryDao.insert(category)
        products.forEach {
            productDao.insert(it)
        }

        val result = productDao.getByIds(listOf(1, 2, 3, 4))

        assertEquals(products, result)
    }

    @Test
    @Throws(Exception::class)
    fun getByIds_returnsRequestedProducts_whenThereAllIdsPresent() = runTest {
        val category = createCategory(1)
        val products = listOf(
            createProduct(1, category.id),
            createProduct(2, category.id),
            createProduct(3, category.id)
        )
        categoryDao.insert(category)
        products.forEach {
            productDao.insert(it)
        }

        val result = productDao.getByIds(listOf(1, 2))

        assertEquals(products.filter { it.id in listOf(1L, 2L) }, result)
    }

    @Test
    @Throws(Exception::class)
    fun getMinPrice_returnsNull_whenThereAreNoProducts() = runTest {
        val result = productDao.getMinPrice()

        assertNull(result)
    }

    @Test
    @Throws(Exception::class)
    fun getMinPrice_returnsMinimalPriceWithDiscount_whenThereAreProducts() = runTest {
        val category = createCategory(1)
        val products = listOf(
            createProduct(1, category.id,
                price = 2f,
                priceWithDiscount = 2f
            ),
            createProduct(2, category.id,
                price = 4f,
                priceWithDiscount = 1f
            ),
            createProduct(3, category.id,
                price = 3f,
                priceWithDiscount = 3f
            )
        )
        categoryDao.insert(category)
        products.forEach {
            productDao.insert(it)
        }

        val result = productDao.getMinPrice()

        assertEquals(1f, result)
    }

    @Test
    @Throws(Exception::class)
    fun getMaxPrice_returnsNull_whenThereAreNoProducts() = runTest {
        val result = productDao.getMaxPrice()

        assertNull(result)
    }

    @Test
    @Throws(Exception::class)
    fun getMaxPrice_returnsMaximalPriceWithDiscount_whenThereAreProducts() = runTest {
        val category = createCategory(1)
        val products = listOf(
            createProduct(1, category.id,
                price = 2f,
                priceWithDiscount = 2f
            ),
            createProduct(2, category.id,
                price = 4f,
                priceWithDiscount = 1f
            ),
            createProduct(3, category.id,
                price = 5f,
                priceWithDiscount = 3f
            )
        )
        categoryDao.insert(category)
        products.forEach {
            productDao.insert(it)
        }

        val result = productDao.getMaxPrice()

        assertEquals(3f, result)
    }

    @After
    @Throws(IOException::class)
    fun after() {
        database.close()
    }
}
