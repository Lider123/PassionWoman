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
import ru.babaetskv.passionwoman.data.database.dao.ColorDao
import ru.babaetskv.passionwoman.data.database.dao.ProductDao
import ru.babaetskv.passionwoman.data.database.dao.ProductItemDao
import java.io.IOException

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class ProductItemDaoInstrumentedTest : DaoInstrumentedTest() {
    private lateinit var database: PassionWomanDatabase
    private lateinit var productItemDao: ProductItemDao
    private lateinit var colorDao: ColorDao
    private lateinit var productDao: ProductDao
    private lateinit var categoryDao: CategoryDao

    @Before
    fun before() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, PassionWomanDatabase::class.java)
            .build()
        productItemDao = database.productItemDao
        colorDao = database.colorDao
        productDao = database.productDao
        categoryDao = database.categoryDao
    }

    @Test
    @Throws(Exception::class)
    fun getByProductId_returnsEmpty_whenThereAreNoProductItems() = runTest {
        val result = productItemDao.getByProductId(1)

        assertTrue(result.isEmpty())
    }

    @Test
    @Throws(Exception::class)
    fun getByProductId_returnsEmpty_whenThereAreNoProductItemsWithRequiredProductId() = runTest {
        val color = createColor(1)
        val category = createCategory(1)
        val product = createProduct(1, category.id)
        val productItem = createProductItem(1, product.id, color.id)
        colorDao.insert(color)
        categoryDao.insert(category)
        productDao.insert(product)
        productItemDao.insert(productItem)

        val result = productItemDao.getByProductId(2)

        assertTrue(result.isEmpty())
    }

    @Test
    @Throws(Exception::class)
    fun getByProductId_returnsProductItemsWithRequiredProductId_whenThereAreProductItemsWithRequiredProductId() =
        runTest {
            val color = createColor(1)
            val category = createCategory(1)
            val products = listOf(
                createProduct(1, category.id),
                createProduct(2, category.id)
            )
            val productItems = listOf(
                createProductItem(1, products[0].id, color.id),
                createProductItem(2, products[0].id, color.id),
                createProductItem(3, products[1].id, color.id),
                createProductItem(4, products[1].id, color.id)
            )
            colorDao.insert(color)
            categoryDao.insert(category)
            products.forEach {
                productDao.insert(it)
            }
            productItems.forEach {
                productItemDao.insert(it)
            }

            val result = productItemDao.getByProductId(1)

            assertEquals(productItems.filter { it.productId == 1 }, result)
        }

    @After
    @Throws(IOException::class)
    fun after() {
        database.close()
    }
}
