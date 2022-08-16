package ru.babaetskv.passionwoman.data.api

import android.content.res.AssetManager
import com.squareup.moshi.Moshi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import retrofit2.HttpException
import ru.babaetskv.passionwoman.data.database.PassionWomanDatabase
import ru.babaetskv.passionwoman.data.database.dao.ProductDao
import ru.babaetskv.passionwoman.data.database.entity.ProductEntity
import ru.babaetskv.passionwoman.data.model.CategoryModel
import ru.babaetskv.passionwoman.data.model.ProductModel

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class CommonApiImplTest {
    private val assetManagerMock: AssetManager = mock()
    private val moshi: Moshi = Moshi.Builder()
        .build()
    private val productDaoMock: ProductDao = mock()
    private val databaseMock: PassionWomanDatabase = mock {
        whenever(mock.productDao) doReturn productDaoMock
    }
    private val productTransformableParamsProviderMock: ProductEntity.TransformableParamsProvider =
        mock()
    private val api =
        CommonApiImpl(
            assetManagerMock,
            databaseMock,
            productTransformableParamsProviderMock,
            moshi
        )

    @Test
    fun getProductByIds_returnsEmpty_whenIdsAreEmpty() = runTest {
        val result = api.getProductsByIds("")

        assertTrue(result.isEmpty())
    }

    @Test
    fun getProductByIds_throwsBadRequest_whenIdsAreWrongFormatted() = runTest {
        listOf(
            "1 2",
            "1!2",
            "1@2",
            "1`2",
            "1~2",
            "1$2",
            "1%2",
            "1^2",
            "1&2",
            "1*2",
            "1(2",
            "1)2",
            "1_2",
            "1-2",
            "1+2",
            "1=2",
            "1{2",
            "1}2",
            "1[2",
            "1]2",
            "1|2",
            "1;2",
            "1:2",
            "1'2",
            "1<2",
            "1.2",
            "1>2",
            "1?2",
            "1/2",
            "1,abc",
        ).forEach {
            try {
                api.getProductsByIds(it)
                fail()
            } catch (e: HttpException) {
                assertEquals(400, e.code())
                assertEquals("Wrong ids list formatting", e.response()?.errorBody()?.string())
            }
        }
    }

    @Test
    fun getProductByIds_doesNotThrow_whenIdsAreWellFormatted() = runTest {
        val category = CategoryModel(
            id = 1,
            name = "Category 1",
            image = "category_1_image_path"
        )
        whenever(productDaoMock.getByIds(any())).thenAnswer { invocation ->
            val ids: Collection<Int> = invocation.getArgument(0)
            ids.map {
                ProductEntity(
                    id = it,
                    categoryId = 1,
                    brandId = null,
                    name = "Product $1",
                    description = null,
                    previewPath = "product_${it}_preview_path",
                    price = 1f,
                    priceWithDiscount = 1f,
                    rating = 0f
                )
            }
        }
        whenever(productTransformableParamsProviderMock.provideCategory(any())) doReturn category
        whenever(productTransformableParamsProviderMock.provideProductItems(any())) doReturn emptyList()

        listOf(
            "1,2",
            "1",
            "1,2,3"
        ).forEach {
            api.getProductsByIds(it)
        }
    }

    @Test
    fun getProductByIds_throwsNotFound_whenThereAreNoProductWithRequiredIdInTheDatabase() =
        runTest {
            val category = CategoryModel(
                id = 1,
                name = "Category 1",
                image = "category_1_image_path"
            )
            val product = ProductEntity(
                id = 1,
                categoryId = category.id,
                brandId = null,
                name = "Product $1",
                description = null,
                previewPath = "product_1_preview_path",
                price = 1f,
                priceWithDiscount = 1f,
                rating = 0f
            )
            whenever(productDaoMock.getByIds(setOf(1, 2))) doReturn listOf(product)
            whenever(productTransformableParamsProviderMock.provideCategory(any())) doReturn category
            whenever(productTransformableParamsProviderMock.provideProductItems(any())) doReturn emptyList()

            try {
                api.getProductsByIds("1,2")
                fail()
            } catch (e: HttpException) {
                assertEquals(404, e.code())
                assertEquals(
                    "One or more products with specified ids are not found",
                    e.response()?.errorBody()?.string()
                )
            }
        }

    @Test
    fun getProductByIds_returnsProducts_whenThereAreProductWithRequiredIdsInTheDatabase() =
        runTest {
            val categoryModel = CategoryModel(
                id = 1,
                image = "category_1_image_path",
                name = "Category 1"
            )
            val products = listOf(
                ProductEntity(
                    id = 1,
                    categoryId = 1,
                    brandId = null,
                    name = "Product 1",
                    description = null,
                    previewPath = "product_1_preview_path",
                    price = 1f,
                    priceWithDiscount = 1f,
                    rating = 0f
                ),
                ProductEntity(
                    id = 2,
                    categoryId = 1,
                    brandId = null,
                    name = "Product 2",
                    description = null,
                    previewPath = "product_2_preview_path",
                    price = 1f,
                    priceWithDiscount = 1f,
                    rating = 0f
                )
            )
            whenever(productDaoMock.getByIds(setOf(1, 2))).doReturn(products)
            whenever(productTransformableParamsProviderMock.provideCategory(any())) doReturn categoryModel
            whenever(productTransformableParamsProviderMock.provideProductItems(any())) doReturn emptyList()

            val result = api.getProductsByIds("1,2")

            val expectedResult = listOf(
                ProductModel(
                    id = 1,
                    additionalInfo = null,
                    brand = null,
                    category = categoryModel,
                    description = null,
                    items = emptyList(),
                    name = "Product 1",
                    preview = "product_1_preview_path",
                    price = 1f,
                    priceWithDiscount = 1f,
                    rating = 0f
                ),
                ProductModel(
                    id = 2,
                    additionalInfo = null,
                    brand = null,
                    category = categoryModel,
                    description = null,
                    items = emptyList(),
                    name = "Product 2",
                    preview = "product_2_preview_path",
                    price = 1f,
                    priceWithDiscount = 1f,
                    rating = 0f
                )
            )
            assertEquals(expectedResult, result)
        }

    // TODO: add test cases for other methods
}
