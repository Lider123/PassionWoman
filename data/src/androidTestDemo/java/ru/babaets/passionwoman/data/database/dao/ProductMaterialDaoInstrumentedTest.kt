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
import ru.babaetskv.passionwoman.data.database.dao.MaterialToProductDao
import ru.babaetskv.passionwoman.data.database.dao.ProductDao
import ru.babaetskv.passionwoman.data.database.dao.ProductMaterialDao
import java.io.IOException

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class ProductMaterialDaoInstrumentedTest : DaoInstrumentedTest() {
    private lateinit var database: PassionWomanDatabase
    private lateinit var productMaterialDao: ProductMaterialDao
    private lateinit var materialToProductDao: MaterialToProductDao
    private lateinit var productDao: ProductDao
    private lateinit var categoryDao: CategoryDao

    @Before
    fun before() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, PassionWomanDatabase::class.java)
            .build()
        productMaterialDao = database.productMaterialDao
        materialToProductDao = database.materialToProductDao
        productDao = database.productDao
        categoryDao = database.categoryDao
    }

    @Test
    @Throws(Exception::class)
    fun getAll_returnsEmpty_whenThereAreNoMaterials() = runTest {
        val result = productMaterialDao.getAll()

        assertTrue(result.isEmpty())
    }

    @Test
    @Throws(Exception::class)
    fun getAll_returnsProductMaterials_whenThereAreMaterials() = runTest {
        val materials = listOf(
            createProductMaterial(1),
            createProductMaterial(2)
        )
        productMaterialDao.insert(*materials.toTypedArray())

        val result = productMaterialDao.getAll()

        assertEquals(materials, result)
    }

    @Test
    @Throws(Exception::class)
    fun getForProduct_returnsEmpty_whenThereAreNoMaterials() = runTest {
        val result = productMaterialDao.getForProduct(1)

        assertTrue(result.isEmpty())
    }

    @Test
    @Throws(Exception::class)
    fun getForProduct_returnsEmpty_whenThereAreNoMaterialToProductEntities() = runTest {
        val materials = listOf(
            createProductMaterial(1),
            createProductMaterial(2)
        )
        productMaterialDao.insert(*materials.toTypedArray())

        val result = productMaterialDao.getForProduct(1)

        assertTrue(result.isEmpty())
    }

    @Test
    @Throws(Exception::class)
    fun getForProduct_returnsMaterialsForProduct_whenThereAreMaterialToProductEntities() = runTest {
        val materials = listOf(
            createProductMaterial(1),
            createProductMaterial(2),
            createProductMaterial(3),
            createProductMaterial(4)
        )
        val category = createCategory(1)
        val products = listOf(
            createProduct(1, category.id),
            createProduct(2, category.id)
        )
        val materialToProductEntities = listOf(
            createMaterialToProductEntity(1, 1, 1),
            createMaterialToProductEntity(2, 2, 1),
            createMaterialToProductEntity(3, 3, 2),
            createMaterialToProductEntity(4, 4, 2)
        )
        categoryDao.insert(category)
        productDao.insert(*products.toTypedArray())
        productMaterialDao.insert(*materials.toTypedArray())
        materialToProductDao.insert(*materialToProductEntities.toTypedArray())

        val result = productMaterialDao.getForProduct(2)

        val expected = listOf(
            createProductMaterial(3).code,
            createProductMaterial(4).code
        )
        assertEquals(expected, result)
    }

    @After
    @Throws(IOException::class)
    fun after() {
        database.close()
    }
}
