package ru.babaets.passionwoman.data.database.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import ru.babaets.passionwoman.data.database.dao.base.DaoInstrumentedTest
import ru.babaetskv.passionwoman.data.database.PassionWomanDatabase
import ru.babaetskv.passionwoman.data.database.dao.*

@ExperimentalCoroutinesApi
class ProductSizeDaoInstrumentedTest : DaoInstrumentedTest() {
    private lateinit var database: PassionWomanDatabase
    private lateinit var productSizeDao: ProductSizeDao
    private lateinit var sizeToProductDao: SizeToProductDao
    private lateinit var productItemDao: ProductItemDao
    private lateinit var colorDao: ColorDao
    private lateinit var productDao: ProductDao
    private lateinit var categoryDao: CategoryDao

    @Before
    fun before() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, PassionWomanDatabase::class.java)
            .build()
        productSizeDao = database.productSizeDao
        sizeToProductDao = database.sizeToProductDao
        productItemDao = database.productItemDao
        colorDao = database.colorDao
        productDao = database.productDao
        categoryDao = database.categoryDao
    }

    @Test
    fun getAll_returnsEmptyList_whenThereAreNoSizes() = runTest {
        val result = productSizeDao.getAll()

        assertTrue(result.isEmpty())
    }

    @Test
    fun getAll_returnsAllSizes_whenThereAreSizes() = runTest {
        val sizes = listOf(
            createProductSize(1),
            createProductSize(2)
        )
        productSizeDao.insert(*sizes.toTypedArray())

        val result = productSizeDao.getAll()

        assertEquals(sizes, result)
    }

    @Test
    fun getForProductItem_returnsEmpty_whenThereAreNoSizes() = runTest {
        val result = productSizeDao.getForProductItem(1)

        assertTrue(result.isEmpty())
    }

    @Test
    fun getForProductItem_returnsEmpty_whenThereAreNoSizeToProductEntities() = runTest {
        val sizes = listOf(
            createProductSize(1),
            createProductSize(2)
        )
        productSizeDao.insert(*sizes.toTypedArray())

        val result = productSizeDao.getForProductItem(1)

        assertTrue(result.isEmpty())
    }

    @Test
    fun getForProductItem_returnsEmpty_whenThereAreNoSizesForRequiredProductItemId() = runTest {
        val sizes = listOf(
            createProductSize(1),
            createProductSize(2)
        )
        val color = createColor(1)
        val category = createCategory(1)
        val product = createProduct(1, category.id)
        val productItem = createProductItem(1, product.id, color.id)
        val sizeToProductEntities = listOf(
            createSizeToProductItemEntity(1, sizes[0].code, productItem.id),
            createSizeToProductItemEntity(2, sizes[1].code, productItem.id),
        )
        productSizeDao.insert(*sizes.toTypedArray())
        categoryDao.insert(category)
        productDao.insert(product)
        colorDao.insert(color)
        productItemDao.insert(productItem)
        sizeToProductDao.insert(*sizeToProductEntities.toTypedArray())


        val result = productSizeDao.getForProductItem(2)

        assertTrue(result.isEmpty())
    }

    @Test
    fun getForProductItem_returnsSizeCodesForRequiredProductItemId_whenThereAreSizesForRequiredProductItemId() =
        runTest {
            val sizes = listOf(
                createProductSize(1),
                createProductSize(2),
                createProductSize(3),
                createProductSize(4)
            )
            val color = createColor(1)
            val category = createCategory(1)
            val product = createProduct(1, category.id)
            val productItems = listOf(
                createProductItem(1, product.id, color.id),
                createProductItem(2, product.id, color.id)
            )
            val sizeToProductEntities = listOf(
                createSizeToProductItemEntity(1, sizes[0].code, productItems[0].id),
                createSizeToProductItemEntity(2, sizes[1].code, productItems[0].id),
                createSizeToProductItemEntity(3, sizes[2].code, productItems[1].id),
                createSizeToProductItemEntity(4, sizes[3].code, productItems[1].id),
            )
            productSizeDao.insert(*sizes.toTypedArray())
            categoryDao.insert(category)
            productDao.insert(product)
            colorDao.insert(color)
            productItemDao.insert(*productItems.toTypedArray())
            sizeToProductDao.insert(*sizeToProductEntities.toTypedArray())


            val result = productSizeDao.getForProductItem(1)

            val expected = listOf(
                createProductSize(1).code,
                createProductSize(2).code
            )
            assertEquals(expected, result)
        }

    @Test
    fun getAvailableForProductItem_returnsEmpty_whenThereAreNoSizes() = runTest {
        val result = productSizeDao.getAvailableForProductItem(1)

        assertTrue(result.isEmpty())
    }

    @Test
    fun getAvailableForProductItem_returnsEmpty_whenThereAreNoSizeToProductEntities() = runTest {
        val sizes = listOf(
            createProductSize(1),
            createProductSize(2)
        )
        productSizeDao.insert(*sizes.toTypedArray())

        val result = productSizeDao.getAvailableForProductItem(1)

        assertTrue(result.isEmpty())
    }

    @Test
    fun getAvailableForProductItem_returnsEmpty_whenThereAreNoSizesForRequiredProductItemId() = runTest {
        val sizes = listOf(
            createProductSize(1),
            createProductSize(2)
        )
        val color = createColor(1)
        val category = createCategory(1)
        val product = createProduct(1, category.id)
        val productItem = createProductItem(1, product.id, color.id)
        val sizeToProductEntities = listOf(
            createSizeToProductItemEntity(1, sizes[0].code, productItem.id),
            createSizeToProductItemEntity(2, sizes[1].code, productItem.id),
        )
        productSizeDao.insert(*sizes.toTypedArray())
        categoryDao.insert(category)
        productDao.insert(product)
        colorDao.insert(color)
        productItemDao.insert(productItem)
        sizeToProductDao.insert(*sizeToProductEntities.toTypedArray())


        val result = productSizeDao.getAvailableForProductItem(2)

        assertTrue(result.isEmpty())
    }

    @Test
    fun getAvailableForProductItem_returnsEmpty_whenThereAreNoAvailableSizesForRequiredProductItemId() = runTest {
        val sizes = listOf(
            createProductSize(1),
            createProductSize(2)
        )
        val color = createColor(1)
        val category = createCategory(1)
        val product = createProduct(1, category.id)
        val productItems = listOf(
            createProductItem(1, product.id, color.id),
            createProductItem(2, product.id, color.id)
        )
        val sizeToProductEntities = listOf(
            createSizeToProductItemEntity(1, sizes[0].code, productItems[0].id, isAvailable = false),
            createSizeToProductItemEntity(2, sizes[1].code, productItems[1].id, isAvailable = true),
        )
        productSizeDao.insert(*sizes.toTypedArray())
        categoryDao.insert(category)
        productDao.insert(product)
        colorDao.insert(color)
        productItemDao.insert(*productItems.toTypedArray())
        sizeToProductDao.insert(*sizeToProductEntities.toTypedArray())


        val result = productSizeDao.getAvailableForProductItem(1)

        assertTrue(result.isEmpty())
    }

    @Test
    fun getAvailableForProductItem_returnsSizeCodesForRequiredProductItemId_whenThereAreAvailableSizesForRequiredProductItemId() =
        runTest {
            val sizes = listOf(
                createProductSize(1),
                createProductSize(2),
                createProductSize(3),
                createProductSize(4)
            )
            val color = createColor(1)
            val category = createCategory(1)
            val product = createProduct(1, category.id)
            val productItems = listOf(
                createProductItem(1, product.id, color.id),
                createProductItem(2, product.id, color.id)
            )
            val sizeToProductEntities = listOf(
                createSizeToProductItemEntity(1, sizes[0].code, productItems[0].id, isAvailable = true),
                createSizeToProductItemEntity(2, sizes[1].code, productItems[0].id, isAvailable = false),
                createSizeToProductItemEntity(3, sizes[2].code, productItems[1].id, isAvailable = true),
                createSizeToProductItemEntity(4, sizes[3].code, productItems[1].id, isAvailable = false),
            )
            productSizeDao.insert(*sizes.toTypedArray())
            categoryDao.insert(category)
            productDao.insert(product)
            colorDao.insert(color)
            productItemDao.insert(*productItems.toTypedArray())
            sizeToProductDao.insert(*sizeToProductEntities.toTypedArray())


            val result = productSizeDao.getAvailableForProductItem(1)

            val expected = listOf(
                createProductSize(1).code
            )
            assertEquals(expected, result)
        }
}
