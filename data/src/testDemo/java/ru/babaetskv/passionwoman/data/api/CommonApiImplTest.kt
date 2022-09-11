package ru.babaetskv.passionwoman.data.api

import android.content.res.AssetManager
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.json.JSONArray
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.*
import retrofit2.HttpException
import ru.babaetskv.passionwoman.data.database.PassionWomanDatabase
import ru.babaetskv.passionwoman.data.database.dao.ProductDao
import ru.babaetskv.passionwoman.data.database.entity.ProductEntity
import ru.babaetskv.passionwoman.data.model.CategoryModel
import ru.babaetskv.passionwoman.data.model.ProductModel
import ru.babaetskv.passionwoman.data.model.StoryModel

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class CommonApiImplTest {
    private val assetManagerMock: AssetManager = mock()
    private val moshi: Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
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
            image = "asset:///demo_db_editor/static/image/category_1.jpg"
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
                    previewPath = "static/image/product_${it}_preview.jpg",
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
                image = "asset:///demo_db_editor/static/image/category_1.jpg"
            )
            val product = ProductEntity(
                id = 1,
                categoryId = category.id,
                brandId = null,
                name = "Product $1",
                description = null,
                previewPath = "static/image/product_1_preview.jpg",
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
                image = "file:///android_asset/demo_db_editor/static/image/category_1.jpg",
                name = "Category 1"
            )
            val products = listOf(
                ProductEntity(
                    id = 1,
                    categoryId = 1,
                    brandId = null,
                    name = "Product 1",
                    description = null,
                    previewPath = "static/image/product_1_preview.jpg",
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
                    previewPath = "static/image/product_2_preview.jpg",
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
                    preview = "file:///android_asset/demo_db_editor/static/image/product_1_preview.jpg",
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
                    preview = "file:///android_asset/demo_db_editor/static/image/product_2_preview.jpg",
                    price = 1f,
                    priceWithDiscount = 1f,
                    rating = 0f
                )
            )
            assertEquals(expectedResult, result)
        }

    @Test
    fun getStories_returnsEmpty_whenThereAreNoStories() = runTest {
        val istream = "[]".byteInputStream()
        whenever(assetManagerMock.open(any())) doReturn istream

        val result = api.getStories()

        assertTrue(result.isEmpty())
    }

    @Test
    fun getStories_throwsInternalServerError_whenStoriesFileIsCorrupted() = runTest {
        val istream = "1234".byteInputStream()
        whenever(assetManagerMock.open(any())) doReturn istream

        try {
            api.getStories()
            fail()
        } catch (e: HttpException) {
            assertEquals(500, e.code())
            assertEquals("Stories source is corrupted", e.response()?.errorBody()?.string())
        }
    }

    @Test
    fun getStories_throwsInternalServerError_whenThereAreStoriesWithoutContent() = runTest {
        val istream = """
            [
              {
                "id": "story_1",
                "image": "file:///android_asset/image/story_1.png",
                "contents": []
              }
            ]
        """.let(::JSONArray).toString().byteInputStream()
        whenever(assetManagerMock.open(any())) doReturn istream

        try {
            api.getStories()
            fail()
        } catch (e: HttpException) {
            assertEquals(500, e.code())
            assertEquals("Stories without content are not allowed", e.response()?.errorBody()?.string())
        }
    }

    @Test
    fun getStories_returnsStories_whenThereAreStoriesWithContent() = runTest {
        val istream = """
            [
              {
                "id": "story_1",
                "image": "story_1_image",
                "contents": [
                  {
                    "id": "story_1_content_1",
                    "title": "Story 1 content 1",
                    "text": "Story 1 content 1 text",
                    "media": "story_1_content_1_media",
                    "type": "video"
                  }
                ]
              }
            ]
        """.trimIndent().byteInputStream()
        whenever(assetManagerMock.open(any())) doReturn istream

        val result = api.getStories()

        val expected = listOf(
            StoryModel(
                id = "story_1",
                banner = "story_1_image",
                contents = listOf(
                    StoryModel.ContentModel(
                        id = "story_1_content_1",
                        title = "Story 1 content 1",
                        text = "Story 1 content 1 text",
                        media = "story_1_content_1_media",
                        type = "video"
                    )
                )
            )
        )
        assertEquals(expected, result)
    }

    // TODO: add test cases for other methods
}
