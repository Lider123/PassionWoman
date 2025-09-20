package ru.babaetskv.passionwoman.data.api

import com.squareup.moshi.JsonDataException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.*
import retrofit2.HttpException
import ru.babaetskv.passionwoman.data.assets.AssetProvider
import ru.babaetskv.passionwoman.data.api.exception.ApiExceptionProvider
import ru.babaetskv.passionwoman.data.database.PassionWomanDatabase
import ru.babaetskv.passionwoman.data.database.dao.ProductDao
import ru.babaetskv.passionwoman.data.database.entity.ProductEntity
import ru.babaetskv.passionwoman.data.model.CategoryModel
import ru.babaetskv.passionwoman.data.model.ProductModel
import ru.babaetskv.passionwoman.data.model.StoryModel

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class CommonApiImplTest {
    private val assetProviderMock: AssetProvider = mock()
    private val exceptionProviderMock: ApiExceptionProvider = mock()
    private val productDaoMock: ProductDao = mock()
    private val databaseMock: PassionWomanDatabase = mock {
        whenever(mock.productDao) doReturn productDaoMock
    }
    private val productTransformableParamsProviderMock: ProductEntity.TransformableParamsProvider =
        mock()
    private val api =
        CommonApiImpl(
            databaseMock,
            assetProviderMock,
            exceptionProviderMock,
            productTransformableParamsProviderMock
        )

    @Test
    fun getProductsByIds_returnsEmpty_whenIdsAreEmpty() = runTest {
        val result = api.getProductsByIds("")

        assertTrue(result.isEmpty())
    }

    @Test
    fun getProductsByIds_throwsBadRequest_whenIdsAreWrongFormatted() = runTest {
        val exceptionMock: HttpException = mock()
        whenever(exceptionProviderMock.getBadRequestException(any())) doReturn exceptionMock

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
                verify(exceptionProviderMock, times(1))
                    .getBadRequestException("Wrong ids list formatting")
                clearInvocations(exceptionProviderMock)
                assertEquals(exceptionMock, e)
            }
        }
    }

    @Test
    fun getProductsByIds_doesNotThrow_whenIdsAreWellFormatted() = runTest {
        val category = CategoryModel(
            id = 1,
            name = "Category 1",
            image = "asset:///demo_db_editor/static/image/category_1.jpg"
        )
        whenever(productDaoMock.getByIds(any())).thenAnswer { invocation ->
            val ids: Collection<Long> = invocation.getArgument(0)
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
    fun getProductsByIds_throwsNotFound_whenThereAreNoProductWithRequiredIdInTheDatabase() =
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
            val exceptionMock: HttpException = mock()
            whenever(productDaoMock.getByIds(listOf(1, 2))) doReturn listOf(product)
            whenever(productTransformableParamsProviderMock.provideCategory(any())) doReturn category
            whenever(productTransformableParamsProviderMock.provideProductItems(any())) doReturn emptyList()
            whenever(exceptionProviderMock.getNotFoundException(any())) doReturn exceptionMock

            try {
                api.getProductsByIds("1,2")
                fail()
            } catch (e: HttpException) {
                verify(exceptionProviderMock, times(1))
                    .getNotFoundException("One or more products with specified ids are not found")
                assertEquals(exceptionMock, e)
            }
        }

    @Test
    fun getProductsByIds_returnsProducts_whenThereAreProductWithRequiredIdsInTheDatabase() =
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
                    createdAt = 0,
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
                    createdAt = 0,
                    rating = 0f
                )
            )
            whenever(productDaoMock.getByIds(listOf(1, 2))).doReturn(products)
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
                    createdAt = 0,
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
                    createdAt = 0,
                    rating = 0f
                )
            )
            assertEquals(expectedResult, result)
        }

    @Test
    fun getStories_returnsEmpty_whenThereAreNoStories() = runTest {
        whenever(assetProviderMock.loadListFromAsset(AssetProvider.AssetFile.STORIES, StoryModel::class.java)) doReturn emptyList()

        val result = api.getStories()

        assertTrue(result.isEmpty())
    }

    @Test
    fun getStories_throwsInternalServerError_whenStoriesFileIsCorrupted() = runTest {
        val exceptionMock: HttpException = mock()
        whenever(assetProviderMock.loadListFromAsset(AssetProvider.AssetFile.STORIES, StoryModel::class.java)) doThrow JsonDataException()
        whenever(exceptionProviderMock.getInternalServerErrorException(any())) doReturn exceptionMock

        try {
            api.getStories()
            fail()
        } catch (e: HttpException) {
            verify(exceptionProviderMock, times(1))
                .getInternalServerErrorException("Stories source is corrupted")
            assertEquals(exceptionMock, e)
        }
    }

    @Test
    fun getStories_throwsInternalServerError_whenThereAreStoriesWithoutContent() = runTest {
        val stories = listOf(
            StoryModel(
                id = "1",
                banner = "banner",
                contents = emptyList()
            )
        )
        val exceptionMock = mock<HttpException>()
        whenever(assetProviderMock.loadListFromAsset(AssetProvider.AssetFile.STORIES, StoryModel::class.java)) doReturn stories
        whenever(exceptionProviderMock.getInternalServerErrorException(any())) doReturn exceptionMock

        try {
            api.getStories()
            fail()
        } catch (e: HttpException) {
            verify(exceptionProviderMock, times(1))
                .getInternalServerErrorException("Stories without content are not allowed")
            assertEquals(exceptionMock, e)
        }
    }

    @Test
    fun getStories_returnsStories_whenThereAreStoriesWithContent() = runTest {
        val stories = listOf(
            StoryModel(
                id = "story_1",
                banner = "story_1_banner",
                contents = listOf(
                    StoryModel.ContentModel(
                        id = "story_1_content_1",
                        media = "story_1_content_1_media",
                        text = null,
                        title = "Content",
                        type = "image"
                    )
                )
            )
        )
        whenever(assetProviderMock.loadListFromAsset(AssetProvider.AssetFile.STORIES, StoryModel::class.java)) doReturn stories

        val result = api.getStories()

        assertEquals(stories, result)
    }

    // TODO: add test cases for other methods
}
