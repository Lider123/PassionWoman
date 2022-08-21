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
import ru.babaetskv.passionwoman.data.database.dao.ModelToProductDao
import ru.babaetskv.passionwoman.data.database.dao.ProductDao
import ru.babaetskv.passionwoman.data.database.dao.ProductModelDao
import java.io.IOException

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class ProductModelDaoInstrumentedTest : DaoInstrumentedTest() {
    private lateinit var database: PassionWomanDatabase
    private lateinit var productModelDao: ProductModelDao
    private lateinit var productDao: ProductDao
    private lateinit var categoryDao: CategoryDao
    private lateinit var modelToProductDao: ModelToProductDao

    @Before
    fun before() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, PassionWomanDatabase::class.java)
            .build()
        productModelDao = database.productModelDao
        productDao = database.productDao
        categoryDao = database.categoryDao
        modelToProductDao = database.modelToProductDao
    }

    @Test
    @Throws(Exception::class)
    fun getAll_returnsEmpty_whenThereAreNoModels() = runTest {
        val result = productModelDao.getAll()

        assertTrue(result.isEmpty())
    }

    @Test
    @Throws(Exception::class)
    fun getAll_returnsModels_whenThereAreModels() = runTest {
        val models = listOf(
            createProductModel(1),
            createProductModel(2)
        )
        productModelDao.insert(*models.toTypedArray())

        val result = productModelDao.getAll()

        assertEquals(models, result)
    }

    @Test
    @Throws(Exception::class)
    fun getCodesForProduct_returnsEmpty_whenThereAreNoModels() = runTest {
        val result = productModelDao.getCodesForProduct(1)

        assertTrue(result.isEmpty())
    }

    @Test
    @Throws(Exception::class)
    fun getCodesForProduct_returnsEmpty_whenThereAreNoProducts() = runTest {
        val models = listOf(
            createProductModel(1),
            createProductModel(2)
        )
        productModelDao.insert(*models.toTypedArray())

        val result = productModelDao.getCodesForProduct(1)

        assertTrue(result.isEmpty())
    }

    @Test
    @Throws(Exception::class)
    fun getCodesForProduct_returnsEmpty_whenThereAreNoModelToProductEntities() = runTest {
        val models = listOf(
            createProductModel(1),
            createProductModel(2)
        )
        val category = createCategory(1)
        val product = createProduct(1, category.id)
        productModelDao.insert(*models.toTypedArray())
        categoryDao.insert(category)
        productDao.insert(product)

        val result = productModelDao.getCodesForProduct(1)

        assertTrue(result.isEmpty())
    }

    @Test
    @Throws(Exception::class)
    fun getCodesForProduct_returnsEmpty_whenThereAreNoModesForRequiredProductId() = runTest {
        val models = listOf(
            createProductModel(1),
            createProductModel(2)
        )
        val category = createCategory(1)
        val products = listOf(
            createProduct(1, category.id),
            createProduct(2, category.id)
        )
        val modelToProductEntities = listOf(
            createModelToProductEntity(1, 1, 1),
            createModelToProductEntity(2, 2, 1)
        )
        productModelDao.insert(*models.toTypedArray())
        categoryDao.insert(category)
        productDao.insert(*products.toTypedArray())
        modelToProductDao.insert(*modelToProductEntities.toTypedArray())

        val result = productModelDao.getCodesForProduct(2)

        assertTrue(result.isEmpty())
    }

    @Test
    @Throws(Exception::class)
    fun getCodesForProduct_returnsModelsForRequiredProductId_whenThereAreModesForRequiredProductId() = runTest {
        val models = listOf(
            createProductModel(1),
            createProductModel(2),
            createProductModel(3),
            createProductModel(4)
        )
        val category = createCategory(1)
        val products = listOf(
            createProduct(1, category.id),
            createProduct(2, category.id)
        )
        val modelToProductEntities = listOf(
            createModelToProductEntity(1, 1, 1),
            createModelToProductEntity(2, 2, 1),
            createModelToProductEntity(3, 3, 2),
            createModelToProductEntity(4, 4, 2)
        )
        productModelDao.insert(*models.toTypedArray())
        categoryDao.insert(category)
        productDao.insert(*products.toTypedArray())
        modelToProductDao.insert(*modelToProductEntities.toTypedArray())

        val result = productModelDao.getCodesForProduct(2)

        val expected = listOf("model3", "model4")
        assertEquals(expected, result)
    }

    @After
    @Throws(IOException::class)
    fun after() {
        database.close()
    }
}
