package ru.babaetskv.passionwoman.data.api.decorator

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.currentTime
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.*
import ru.babaetskv.passionwoman.data.api.CommonApi
import ru.babaetskv.passionwoman.data.model.*

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class DelayCommonApiDecoratorTest {
    @Mock
    lateinit var apiMock: CommonApi
    @InjectMocks
    lateinit var decorator: DelayCommonApiDecorator

    @Test
    fun authorize_callsApi() = runTest {
        val accessToken = AccessTokenModel("token")

        decorator.authorize(accessToken)

        verify(apiMock, times(1))
            .authorize(accessToken)
    }

    @Test
    fun authorize_returnsAuthTokenFromApi() = runTest {
        val accessToken = AccessTokenModel("token")
        val authToken = AuthTokenModel("token")
        whenever(apiMock.authorize(accessToken)) doReturn authToken

        val result = decorator.authorize(accessToken)

        assertEquals(authToken, result)
    }

    @Test
    fun authorize_hasMinimumExecutionTimeOf500() = runTest {
        val accessToken = AccessTokenModel("token")

        val executionTime = executionTime {
            decorator.authorize(accessToken)
        }

        assertTrue(executionTime >= 500)
    }

    @Test
    fun getCategories_callsApi() = runTest {
        decorator.getCategories()

        verify(apiMock, times(1))
            .getCategories()
    }

    @Test
    fun getCategories_returnsCategoriesFromApi() = runTest {
        val categories = listOf(
            createCategory()
        )
        whenever(apiMock.getCategories()) doReturn categories

        val result = decorator.getCategories()

        assertEquals(categories, result)
    }

    @Test
    fun getCategories_hasMinimumExecutionTimeOf500() = runTest {
        val executionTime = executionTime {
            decorator.getCategories()
        }

        assertTrue(executionTime >= 500)
    }

    @Test
    fun getPopularBrands_callsApi() = runTest {
        decorator.getPopularBrands(1)

        verify(apiMock, times(1))
            .getPopularBrands(1)
    }

    @Test
    fun getPopularBrands_returnsBrandsFromApi() = runTest {
        val brands = listOf(
            createBrand()
        )
        whenever(apiMock.getPopularBrands(1)) doReturn brands

        val result = decorator.getPopularBrands(1)

        assertEquals(brands, result)
    }

    @Test
    fun getPopularBrands_hasMinimumExecutionTimeOf500() = runTest {
        val executionTime = executionTime {
            decorator.getPopularBrands(1)
        }

        assertTrue(executionTime >= 500)
    }

    @Test
    fun getProduct_callsApi() = runTest {
        decorator.getProduct(1)

        verify(apiMock, times(1))
            .getProduct(1)
    }

    @Test
    fun getProduct_returnsProductFromApi() = runTest {
        val product = createProduct()
        whenever(apiMock.getProduct(1)) doReturn product

        val result = decorator.getProduct(1)

        assertEquals(product, result)
    }

    @Test
    fun getProduct_hasMinimumExecutionTimeOf500() = runTest {
        val executionTime = executionTime {
            decorator.getProduct(1)
        }

        assertTrue(executionTime >= 500)
    }

    @Test
    fun getProducts_callsApi() = runTest {
        decorator.getProducts(1, "query", "filters", "sorting", 2, 3)

        verify(apiMock, times(1))
            .getProducts(1, "query", "filters", "sorting", 2, 3)
    }

    @Test
    fun getProducts_returnsProductsFromApi() = runTest {
        val response = ProductsPagedResponseModel(
            availableFilters = listOf(),
            products = listOf(
                createProduct()
            ),
            total = 1
        )
        whenever(apiMock.getProducts(1, "query", "filters", "sorting", 2, 3)) doReturn response

        val result = decorator.getProducts(1, "query", "filters", "sorting", 2, 3)

        assertEquals(response, result)
    }

    @Test
    fun getProducts_hasMinimumExecutionTimeOf500() = runTest {
        val executionTime = executionTime {
            decorator.getProducts(1, "query", "filters", "sorting", 2, 3)
        }

        assertTrue(executionTime >= 500)
    }

    @Test
    fun getProductsByIds_callsApi() = runTest {
        decorator.getProductsByIds("ids")

        verify(apiMock, times(1))
            .getProductsByIds("ids")
    }

    @Test
    fun getProductsByIds_returnsProductsFromApi() = runTest {
        val products = listOf(
            createProduct()
        )
        whenever(apiMock.getProductsByIds("ids")) doReturn products

        val result = decorator.getProductsByIds("ids")

        assertEquals(products, result)
    }

    @Test
    fun getProductsByIds_hasMinimumExecutionTimeOf500() = runTest {
        val executionTime = executionTime {
            decorator.getProductsByIds("ids")
        }

        assertTrue(executionTime >= 500)
    }

    @Test
    fun getPromotions_callsApi() = runTest {
        decorator.getPromotions()

        verify(apiMock, times(1))
            .getPromotions()
    }

    @Test
    fun getPromotions_returnsProductsFromApi() = runTest {
        val promotions = listOf(
            createPromotion()
        )
        whenever(apiMock.getPromotions()) doReturn promotions

        val result = decorator.getPromotions()

        assertEquals(promotions, result)
    }

    @Test
    fun getPromotions_hasMinimumExecutionTimeOf500() = runTest {
        val executionTime = executionTime {
            decorator.getPromotions()
        }

        assertTrue(executionTime >= 500)
    }

    @Test
    fun getStories_callsApi() = runTest {
        decorator.getStories()

        verify(apiMock, times(1))
            .getStories()
    }

    @Test
    fun getStories_returnsProductsFromApi() = runTest {
        val stories = listOf(
            createStory()
        )
        whenever(apiMock.getStories()) doReturn stories

        val result = decorator.getStories()

        assertEquals(stories, result)
    }

    @Test
    fun getStories_hasMinimumExecutionTimeOf500() = runTest {
        val executionTime = executionTime {
            decorator.getStories()
        }

        assertTrue(executionTime >= 500)
    }

    private fun createCategory() =
        CategoryModel(
            id = 1,
            name = "category_1",
            image = "category_1_image",
        )

    private fun createBrand() =
        BrandModel(
            id = 1,
            logoPath = "brand_1_logo_path",
            name = "brand_1"
        )

    private fun createProduct() =
        ProductModel(
            id = 1,
            category = createCategory(),
            name = "product_1",
            description = null,
            preview = "product_1_preview",
            price = 1f,
            priceWithDiscount = 1f,
            createdAt = 0,
            rating = 0f,
            brand = null,
            additionalInfo = null,
            items = emptyList()
        )

    private fun createPromotion() =
        PromotionModel(
            id = 1,
            banner = "promotion_1_banner"
        )

    private fun createStory() =
        StoryModel(
            id = "story_1",
            banner = "story_1_banner",
            contents = emptyList()
        )

    private suspend fun TestScope.executionTime(block: suspend () -> Unit): Long {
        val start = currentTime
        block.invoke()
        val end = currentTime
        return end - start
    }
}