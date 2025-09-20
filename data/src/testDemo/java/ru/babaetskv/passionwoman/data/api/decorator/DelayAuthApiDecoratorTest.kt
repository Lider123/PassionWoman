package ru.babaetskv.passionwoman.data.api.decorator

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.currentTime
import kotlinx.coroutines.test.runTest
import okhttp3.MultipartBody
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.*
import ru.babaetskv.passionwoman.data.api.AuthApi
import ru.babaetskv.passionwoman.data.model.*

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class DelayAuthApiDecoratorTest {
    @Mock
    lateinit var apiMock: AuthApi
    @InjectMocks
    lateinit var decorator: DelayAuthApiDecorator

    @Test
    fun addToCart_callsApi() = runTest {
        val cartItemModel = createCartItemModel()

        decorator.addToCart(cartItemModel)

        verify(apiMock, times(1))
            .addToCart(cartItemModel)
    }

    @Test
    fun addToCart_returnsCartFromApi() = runTest {
        val cartItemModel = createCartItemModel()
        val cartModel = createCartModel()
        whenever(apiMock.addToCart(any())) doReturn cartModel

        val result = decorator.addToCart(cartItemModel)

        assertEquals(cartModel, result)
    }

    @Test
    fun addToCart_hasMinimumExecutionTimeOf500() = runTest {
        val cartItemModel = createCartItemModel()

        val executionTime = executionTime {
            decorator.addToCart(cartItemModel)
        }

        assertTrue(executionTime >= 500)
    }

    @Test
    fun checkout_callsApi() = runTest {
        decorator.checkout()

        verify(apiMock, times(1))
            .checkout()
    }

    @Test
    fun checkout_hasMinimumExecutionTimeOf500() = runTest {
        val executionTime = executionTime {
            decorator.checkout()
        }

        assertTrue(executionTime >= 500)
    }

    @Test
    fun getCart_callsApi() = runTest {
        decorator.getCart()

        verify(apiMock, times(1))
            .getCart()
    }

    @Test
    fun getCart_returnsCartFromApi() = runTest {
        val cart = createCartModel()
        whenever(apiMock.getCart()) doReturn cart

        val result = decorator.getCart()

        assertEquals(cart, result)
    }

    @Test
    fun getCart_hasMinimumExecutionTimeOf500() = runTest {
        val executionTime = executionTime {
            decorator.getCart()
        }

        assertTrue(executionTime >= 500)
    }

    @Test
    fun getFavoriteIds_callsApi() = runTest {
        decorator.getFavoriteIds()

        verify(apiMock, times(1))
            .getFavoriteIds()
    }

    @Test
    fun getFavoriteIds_returnsIdsFromApi() = runTest {
        val ids = listOf<Long>(1, 2, 3)
        whenever(apiMock.getFavoriteIds()) doReturn ids

        val result = decorator.getFavoriteIds()

        assertEquals(ids, result)
    }

    @Test
    fun getFavoriteIds_hasMinimumExecutionTimeOf500() = runTest {
        val executionTime = executionTime {
            decorator.getFavoriteIds()
        }

        assertTrue(executionTime >= 500)
    }

    @Test
    fun getOrders_callsApi() = runTest {
        decorator.getOrders()

        verify(apiMock, times(1))
            .getOrders()
    }

    @Test
    fun getOrders_returnsOrdersFromApi() = runTest {
        val orders = listOf(
            createOrder()
        )
        whenever(apiMock.getOrders()) doReturn orders

        val result = decorator.getOrders()

        assertEquals(orders, result)
    }

    @Test
    fun getOrders_hasMinimumExecutionTimeOf500() = runTest {
        val executionTime = executionTime {
            decorator.getOrders()
        }

        assertTrue(executionTime >= 500)
    }

    @Test
    fun getProfile_callsApi() = runTest {
        decorator.getProfile()

        verify(apiMock, times(1))
            .getProfile()
    }

    @Test
    fun getProfile_returnsProfileFromApi() = runTest {
        val profile = createProfile()
        whenever(apiMock.getProfile()) doReturn profile

        val result = decorator.getProfile()

        assertEquals(profile, result)
    }

    @Test
    fun getProfile_hasMinimumExecutionTimeOf500() = runTest {
        val executionTime = executionTime {
            decorator.getProfile()
        }

        assertTrue(executionTime >= 500)
    }

    @Test
    fun removeFromCart_callsApi() = runTest {
        val item = createCartItemModel()

        decorator.removeFromCart(item)

        verify(apiMock, times(1))
            .removeFromCart(item)
    }

    @Test
    fun removeFromCart_returnsCartFromApi() = runTest {
        val item = createCartItemModel()
        val cart = createCartModel()
        whenever(apiMock.removeFromCart(item)) doReturn cart

        val result = decorator.removeFromCart(item)

        assertEquals(cart, result)
    }

    @Test
    fun removeFromCart_hasMinimumExecutionTimeOf500() = runTest {
        val item = createCartItemModel()

        val executionTime = executionTime {
            decorator.removeFromCart(item)
        }

        assertTrue(executionTime >= 500)
    }

    @Test
    fun setFavoriteIds_callsApi() = runTest {
        val ids = listOf<Long>(1, 2, 3)

        decorator.setFavoriteIds(ids)

        verify(apiMock, times(1))
            .setFavoriteIds(ids)
    }

    @Test
    fun setFavoriteIds_hasMinimumExecutionTimeOf500() = runTest {
        val ids = listOf<Long>(1, 2, 3)

        val executionTime = executionTime {
            decorator.setFavoriteIds(ids)
        }

        assertTrue(executionTime >= 500)
    }

    @Test
    fun updateProfile_callsApi() = runTest {
        val profile = createProfile()

        decorator.updateProfile(profile)

        verify(apiMock, times(1))
            .updateProfile(profile)
    }

    @Test
    fun updateProfile_hasMinimumExecutionTimeOf500() = runTest {
        val profile = createProfile()

        val executionTime = executionTime {
            decorator.updateProfile(profile)
        }

        assertTrue(executionTime >= 500)
    }

    @Test
    fun uploadAvatar_callsApi() = runTest {
        val part = MultipartBody.Part.create(mock())

        decorator.uploadAvatar(part)

        verify(apiMock, times(1))
            .uploadAvatar(part)
    }

    @Test
    fun uploadAvatar_hasMinimumExecutionTimeOf500() = runTest {
        val part = MultipartBody.Part.create(mock())

        val executionTime = executionTime {
            decorator.uploadAvatar(part)
        }

        assertTrue(executionTime >= 500)
    }

    private fun createCartItemModel() =
        CartItemModel(
            productId = 1,
            preview = "cart_item_1_preview",
            selectedColor = createColorModel(),
            selectedSize = "",
            name = "cart_item_1",
            price = 1f,
            priceWithDiscount = 0f,
            count = 1
        )

    private fun createColorModel() =
        ColorModel(
            id = 1,
            uiName = "color_1",
            hex = "color_1_hex"
        )

    private fun createCartModel() =
        CartModel(
            items = emptyList(),
            price = 0f,
            total = 0f
        )

    private fun createOrder() =
        OrderModel(
            id = 1,
            createdAt = "",
            cartItems = emptyList(),
            status = ""
        )

    private fun createProfile() =
        ProfileModel(
            id = 1,
            name = "Jane",
            surname = "Doe",
            phone = "",
            avatar = null
        )

    private suspend fun TestScope.executionTime(block: suspend () -> Unit): Long {
        val start = currentTime
        block.invoke()
        val end = currentTime
        return end - start
    }
}
