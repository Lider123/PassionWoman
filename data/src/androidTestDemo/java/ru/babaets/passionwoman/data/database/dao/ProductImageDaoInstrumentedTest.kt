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
import ru.babaetskv.passionwoman.data.database.dao.*
import ru.babaetskv.passionwoman.data.database.entity.*
import java.io.IOException

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class ProductImageDaoInstrumentedTest {
    private lateinit var database: PassionWomanDatabase
    private lateinit var productImageDao: ProductImageDao
    private lateinit var productItemDao: ProductItemDao
    private lateinit var colorDao: ColorDao
    private lateinit var productDao: ProductDao
    private lateinit var categoryDao: CategoryDao

    private fun createProductImage(id: Int, productItemId: Int) =
        ProductImageEntity(
            id = id,
            imagePath = "product_image_${id}_path",
            productItemId = productItemId
        )

    private fun createProductItem(id: Int, productId: Int, colorId: Int) =
        ProductItemEntity(
            id = id,
            productId = productId,
            colorId = colorId
        )

    private fun createColor(id: Int) =
        ColorEntity(
            id = id,
            uiName = "Color $id",
            hex="hex$id"
        )

    private fun createProduct(id: Int, categoryId: Int) =
        ProductEntity(
            id = id,
            brandId = null,
            categoryId = categoryId,
            description = null,
            name = "Product $id",
            previewPath = "product_${id}_preview_path",
            price = 1f,
            priceWithDiscount = 1f,
            rating = 0f
        )

    private fun createCategory(id: Int) =
        CategoryEntity(
            id = id,
            name = "Category $id",
            imagePath = "category_${id}_image_path"
        )

    @Before
    fun before() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, PassionWomanDatabase::class.java)
            .build()
        productImageDao = database.productImageDao
        productItemDao = database.productItemDao
        colorDao = database.colorDao
        productDao = database.productDao
        categoryDao = database.categoryDao
    }

    @Test
    @Throws(Exception::class)
    fun getForProductItem_returnsEmpty_whenThereAreNoImages() = runTest {
        val result = productImageDao.getForProductItem(1)

        assertTrue(result.isEmpty())
    }

    @Test
    @Throws(Exception::class)
    fun getForProductItem_returnsEmpty_whenThereAreNoImagesForRequiredProductItemId() = runTest {
        val color = createColor(1)
        val category = createCategory(1)
        val product = createProduct(1, category.id)
        val productItem = createProductItem(1, product.id, color.id)
        val images = listOf(
            createProductImage(1, productItem.id),
            createProductImage(2, productItem.id)
        )
        colorDao.insert(color)
        categoryDao.insert(category)
        productDao.insert(product)
        productItemDao.insert(productItem)
        images.forEach {
            productImageDao.insert(it)
        }

        val result = productImageDao.getForProductItem(2)

        assertTrue(result.isEmpty())
    }

    @Test
    @Throws(Exception::class)
    fun getForProductItem_returnsProductImagesOfRequiredProductItemId_whenThereAreImagesForRequiredProductItemId() =
        runTest {
            val color = createColor(1)
            val category = createCategory(1)
            val product = createProduct(1, category.id)
            val productItems = listOf(
                createProductItem(1, product.id, color.id),
                createProductItem(2, product.id, color.id)
            )
            val images = listOf(
                createProductImage(1, 1),
                createProductImage(2, 1),
                createProductImage(3, 2),
                createProductImage(4, 2)
            )
            colorDao.insert(color)
            categoryDao.insert(category)
            productDao.insert(product)
            productItems.forEach {
                productItemDao.insert(it)
            }
            images.forEach {
                productImageDao.insert(it)
            }

            val result = productImageDao.getForProductItem(1)

            assertEquals(images.filter { it.productItemId == 1 }.map { it.imagePath }, result)
        }

    @After
    @Throws(IOException::class)
    fun after() {
        database.close()
    }
}
