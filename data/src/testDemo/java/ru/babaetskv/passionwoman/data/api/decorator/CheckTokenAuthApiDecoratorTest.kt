package ru.babaetskv.passionwoman.data.api.decorator

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.MultipartBody
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.*
import retrofit2.HttpException
import ru.babaetskv.passionwoman.data.api.AuthApi
import ru.babaetskv.passionwoman.data.api.exception.ApiExceptionProvider
import ru.babaetskv.passionwoman.data.model.*
import ru.babaetskv.passionwoman.domain.preferences.AuthPreferences
import java.lang.Exception

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class CheckTokenAuthApiDecoratorTest {
    @Mock
    lateinit var authPreferencesMock: AuthPreferences
    @Mock
    lateinit var exceptionProviderMock: ApiExceptionProvider
    @Mock
    lateinit var apiMock: AuthApi
    @InjectMocks
    lateinit var decorator: CheckTokenAuthApiDecorator

    @Before
    fun before() {
        whenever(authPreferencesMock.authToken) doReturn "token"
    }

    @Test
    fun addToCart_throwsUnauthorizedException_ifTokenIsIncorrect() = runTest {
        val item = createCartItem()
        val exceptionMock: HttpException = mock()
        whenever(exceptionProviderMock.getUnauthorizedException(any())) doReturn exceptionMock
        whenever(authPreferencesMock.authToken) doReturn ""

        try {
            decorator.addToCart(item)
            fail()
        } catch (e: HttpException) {
            verify(exceptionProviderMock, times(1))
                .getUnauthorizedException("Unauthorized")
            assertEquals(exceptionMock, e)
        }
    }

    @Test
    fun addToCart_doesNotCallApi_ifTokenIsIncorrect() = runTest {
        val item = createCartItem()
        val exceptionMock: HttpException = mock()
        whenever(exceptionProviderMock.getUnauthorizedException(any())) doReturn exceptionMock
        whenever(authPreferencesMock.authToken) doReturn ""

        try {
            decorator.addToCart(item)
        } catch (e: Exception) {
        }

        verify(apiMock, times(0))
            .addToCart(any())
    }

    @Test
    fun addToCart_callsApi_ifTokenIsCorrect() = runTest {
        val item = createCartItem()

        decorator.addToCart(item)

        verify(apiMock, times(1))
            .addToCart(item)
    }

    @Test
    fun addToCart_returnsCart_ifTokenIsCorrect() = runTest {
        val item = createCartItem()
        val cart = createCart()
        whenever(apiMock.addToCart(item)) doReturn cart

        val result = decorator.addToCart(item)

        assertEquals(cart, result)
    }

    @Test
    fun checkout_throwsUnauthorizedException_ifTokenIsIncorrect() = runTest {
        val exceptionMock: HttpException = mock()
        whenever(exceptionProviderMock.getUnauthorizedException(any())) doReturn exceptionMock
        whenever(authPreferencesMock.authToken) doReturn ""

        try {
            decorator.checkout()
            fail()
        } catch (e: HttpException) {
            verify(exceptionProviderMock, times(1))
                .getUnauthorizedException("Unauthorized")
            assertEquals(exceptionMock, e)
        }
    }

    @Test
    fun checkout_doesNotCallApi_ifTokenIsIncorrect() = runTest {
        val exceptionMock: HttpException = mock()
        whenever(exceptionProviderMock.getUnauthorizedException(any())) doReturn exceptionMock
        whenever(authPreferencesMock.authToken) doReturn ""

        try {
            decorator.checkout()
        } catch (e: Exception) {
        }

        verify(apiMock, times(0))
            .checkout()
    }

    @Test
    fun checkout_callsApi_ifTokenIsCorrect() = runTest {
        decorator.checkout()

        verify(apiMock, times(1))
            .checkout()
    }

    @Test
    fun checkout_returnsCart_ifTokenIsCorrect() = runTest {
        val cart = createCart()
        whenever(apiMock.checkout()) doReturn cart

        val result = decorator.checkout()

        assertEquals(cart, result)
    }

    @Test
    fun getCart_throwsUnauthorizedException_ifTokenIsIncorrect() = runTest {
        val exceptionMock: HttpException = mock()
        whenever(exceptionProviderMock.getUnauthorizedException(any())) doReturn exceptionMock
        whenever(authPreferencesMock.authToken) doReturn ""

        try {
            decorator.getCart()
            fail()
        } catch (e: HttpException) {
            verify(exceptionProviderMock, times(1))
                .getUnauthorizedException("Unauthorized")
            assertEquals(exceptionMock, e)
        }
    }

    @Test
    fun getCart_doesNotCallApi_ifTokenIsIncorrect() = runTest {
        val exceptionMock: HttpException = mock()
        whenever(exceptionProviderMock.getUnauthorizedException(any())) doReturn exceptionMock
        whenever(authPreferencesMock.authToken) doReturn ""

        try {
            decorator.getCart()
        } catch (e: Exception) {
        }

        verify(apiMock, times(0))
            .getCart()
    }

    @Test
    fun getCart_callsApi_ifTokenIsCorrect() = runTest {
        decorator.getCart()

        verify(apiMock, times(1))
            .getCart()
    }

    @Test
    fun getCart_returnsCart_ifTokenIsCorrect() = runTest {
        val cart = createCart()
        whenever(apiMock.getCart()) doReturn cart

        val result = decorator.getCart()

        assertEquals(cart, result)
    }

    @Test
    fun getFavoriteIds_throwsUnauthorizedException_ifTokenIsIncorrect() = runTest {
        val exceptionMock: HttpException = mock()
        whenever(exceptionProviderMock.getUnauthorizedException(any())) doReturn exceptionMock
        whenever(authPreferencesMock.authToken) doReturn ""

        try {
            decorator.getFavoriteIds()
            fail()
        } catch (e: HttpException) {
            verify(exceptionProviderMock, times(1))
                .getUnauthorizedException("Unauthorized")
            assertEquals(exceptionMock, e)
        }
    }

    @Test
    fun getFavoriteIds_doesNotCallApi_ifTokenIsIncorrect() = runTest {
        val exceptionMock: HttpException = mock()
        whenever(exceptionProviderMock.getUnauthorizedException(any())) doReturn exceptionMock
        whenever(authPreferencesMock.authToken) doReturn ""

        try {
            decorator.getFavoriteIds()
        } catch (e: Exception) {
        }

        verify(apiMock, times(0))
            .getFavoriteIds()
    }

    @Test
    fun getFavoriteIds_callsApi_ifTokenIsCorrect() = runTest {
        decorator.getFavoriteIds()

        verify(apiMock, times(1))
            .getFavoriteIds()
    }

    @Test
    fun getFavoriteIds_returnsIds_ifTokenIsCorrect() = runTest {
        val ids = listOf<Long>(1, 2, 3)
        whenever(apiMock.getFavoriteIds()) doReturn ids

        val result = decorator.getFavoriteIds()

        assertEquals(ids, result)
    }

    @Test
    fun getOrders_throwsUnauthorizedException_ifTokenIsIncorrect() = runTest {
        val exceptionMock: HttpException = mock()
        whenever(exceptionProviderMock.getUnauthorizedException(any())) doReturn exceptionMock
        whenever(authPreferencesMock.authToken) doReturn ""

        try {
            decorator.getOrders()
            fail()
        } catch (e: HttpException) {
            verify(exceptionProviderMock, times(1))
                .getUnauthorizedException("Unauthorized")
            assertEquals(exceptionMock, e)
        }
    }

    @Test
    fun getOrders_doesNotCallApi_ifTokenIsIncorrect() = runTest {
        val exceptionMock: HttpException = mock()
        whenever(exceptionProviderMock.getUnauthorizedException(any())) doReturn exceptionMock
        whenever(authPreferencesMock.authToken) doReturn ""

        try {
            decorator.getOrders()
        } catch (e: Exception) {
        }

        verify(apiMock, times(0))
            .getOrders()
    }

    @Test
    fun getOrders_callsApi_ifTokenIsCorrect() = runTest {
        decorator.getOrders()

        verify(apiMock, times(1))
            .getOrders()
    }

    @Test
    fun getOrders_returnsOrders_ifTokenIsCorrect() = runTest {
        val orders = listOf(
            createOrder()
        )
        whenever(apiMock.getOrders()) doReturn orders

        val result = decorator.getOrders()

        assertEquals(orders, result)
    }

    @Test
    fun getProfile_throwsUnauthorizedException_ifTokenIsIncorrect() = runTest {
        val exceptionMock: HttpException = mock()
        whenever(exceptionProviderMock.getUnauthorizedException(any())) doReturn exceptionMock
        whenever(authPreferencesMock.authToken) doReturn ""

        try {
            decorator.getProfile()
            fail()
        } catch (e: HttpException) {
            verify(exceptionProviderMock, times(1))
                .getUnauthorizedException("Unauthorized")
            assertEquals(exceptionMock, e)
        }
    }

    @Test
    fun getProfile_doesNotCallApi_ifTokenIsIncorrect() = runTest {
        val exceptionMock: HttpException = mock()
        whenever(exceptionProviderMock.getUnauthorizedException(any())) doReturn exceptionMock
        whenever(authPreferencesMock.authToken) doReturn ""

        try {
            decorator.getProfile()
        } catch (e: Exception) {
        }

        verify(apiMock, times(0))
            .getProfile()
    }

    @Test
    fun getProfile_callsApi_ifTokenIsCorrect() = runTest {
        decorator.getOrders()

        verify(apiMock, times(1))
            .getOrders()
    }

    @Test
    fun getProfile_returnsProfile_ifTokenIsCorrect() = runTest {
        val profile = createProfile()
        whenever(apiMock.getProfile()) doReturn profile

        val result = decorator.getProfile()

        assertEquals(profile, result)
    }

    @Test
    fun removeFromCart_throwsUnauthorizedException_ifTokenIsIncorrect() = runTest {
        val item = createCartItem()
        val exceptionMock: HttpException = mock()
        whenever(exceptionProviderMock.getUnauthorizedException(any())) doReturn exceptionMock
        whenever(authPreferencesMock.authToken) doReturn ""

        try {
            decorator.removeFromCart(item)
            fail()
        } catch (e: HttpException) {
            verify(exceptionProviderMock, times(1))
                .getUnauthorizedException("Unauthorized")
            assertEquals(exceptionMock, e)
        }
    }

    @Test
    fun removeFromCart_doesNotCallApi_ifTokenIsIncorrect() = runTest {
        val item = createCartItem()
        val exceptionMock: HttpException = mock()
        whenever(exceptionProviderMock.getUnauthorizedException(any())) doReturn exceptionMock
        whenever(authPreferencesMock.authToken) doReturn ""

        try {
            decorator.removeFromCart(item)
        } catch (e: Exception) {
        }

        verify(apiMock, times(0))
            .removeFromCart(any())
    }

    @Test
    fun removeFromCart_callsApi_ifTokenIsCorrect() = runTest {
        val item = createCartItem()

        decorator.removeFromCart(item)

        verify(apiMock, times(1))
            .removeFromCart(item)
    }

    @Test
    fun removeFromCart_returnsCart_ifTokenIsCorrect() = runTest {
        val item = createCartItem()
        val cart = createCart()
        whenever(apiMock.removeFromCart(item)) doReturn cart

        val result = decorator.removeFromCart(item)

        assertEquals(cart, result)
    }

    @Test
    fun setFavoriteIds_throwsUnauthorizedException_ifTokenIsIncorrect() = runTest {
        val ids = listOf<Long>(1, 2, 3)
        val exceptionMock: HttpException = mock()
        whenever(exceptionProviderMock.getUnauthorizedException(any())) doReturn exceptionMock
        whenever(authPreferencesMock.authToken) doReturn ""

        try {
            decorator.setFavoriteIds(ids)
            fail()
        } catch (e: HttpException) {
            verify(exceptionProviderMock, times(1))
                .getUnauthorizedException("Unauthorized")
            assertEquals(exceptionMock, e)
        }
    }

    @Test
    fun setFavoriteIds_doesNotCallApi_ifTokenIsIncorrect() = runTest {
        val ids = listOf<Long>(1, 2, 3)
        val exceptionMock: HttpException = mock()
        whenever(exceptionProviderMock.getUnauthorizedException(any())) doReturn exceptionMock
        whenever(authPreferencesMock.authToken) doReturn ""

        try {
            decorator.setFavoriteIds(ids)
        } catch (e: Exception) {
        }

        verify(apiMock, times(0))
            .setFavoriteIds(any())
    }

    @Test
    fun setFavoriteIds_callsApi_ifTokenIsCorrect() = runTest {
        val ids = listOf<Long>(1, 2, 3)

        decorator.setFavoriteIds(ids)

        verify(apiMock, times(1))
            .setFavoriteIds(ids)
    }

    @Test
    fun updateProfile_throwsUnauthorizedException_ifTokenIsIncorrect() = runTest {
        val profile = createProfile()
        val exceptionMock: HttpException = mock()
        whenever(exceptionProviderMock.getUnauthorizedException(any())) doReturn exceptionMock
        whenever(authPreferencesMock.authToken) doReturn ""

        try {
            decorator.updateProfile(profile)
            fail()
        } catch (e: HttpException) {
            verify(exceptionProviderMock, times(1))
                .getUnauthorizedException("Unauthorized")
            assertEquals(exceptionMock, e)
        }
    }

    @Test
    fun updateProfile_doesNotCallApi_ifTokenIsIncorrect() = runTest {
        val profile = createProfile()
        val exceptionMock: HttpException = mock()
        whenever(exceptionProviderMock.getUnauthorizedException(any())) doReturn exceptionMock
        whenever(authPreferencesMock.authToken) doReturn ""

        try {
            decorator.updateProfile(profile)
        } catch (e: Exception) {
        }

        verify(apiMock, times(0))
            .updateProfile(any())
    }

    @Test
    fun updateProfile_callsApi_ifTokenIsCorrect() = runTest {
        val profile = createProfile()

        decorator.updateProfile(profile)

        verify(apiMock, times(1))
            .updateProfile(profile)
    }

    @Test
    fun uploadAvatar_throwsUnauthorizedException_ifTokenIsIncorrect() = runTest {
        val part = MultipartBody.Part.create(mock())
        val exceptionMock: HttpException = mock()
        whenever(exceptionProviderMock.getUnauthorizedException(any())) doReturn exceptionMock
        whenever(authPreferencesMock.authToken) doReturn ""

        try {
            decorator.uploadAvatar(part)
            fail()
        } catch (e: HttpException) {
            verify(exceptionProviderMock, times(1))
                .getUnauthorizedException("Unauthorized")
            assertEquals(exceptionMock, e)
        }
    }

    @Test
    fun uploadAvatar_doesNotCallApi_ifTokenIsIncorrect() = runTest {
        val part = MultipartBody.Part.create(mock())
        val exceptionMock: HttpException = mock()
        whenever(exceptionProviderMock.getUnauthorizedException(any())) doReturn exceptionMock
        whenever(authPreferencesMock.authToken) doReturn ""

        try {
            decorator.uploadAvatar(part)
        } catch (e: Exception) {
        }

        verify(apiMock, times(0))
            .uploadAvatar(any())
    }

    @Test
    fun uploadAvatar_callsApi_ifTokenIsCorrect() = runTest {
        val part = MultipartBody.Part.create(mock())

        decorator.uploadAvatar(part)

        verify(apiMock, times(1))
            .uploadAvatar(part)
    }

    private fun createCartItem() =
        CartItemModel(
            productId = 1,
            preview = "cart_item_1_preview",
            selectedColor = createColor(),
            selectedSize = "",
            name = "cart_item_1",
            price = 1f,
            priceWithDiscount = 0f,
            count = 1
        )

    private fun createColor() =
        ColorModel(
            id = 1,
            uiName = "color_1",
            hex = "color_1_hex"
        )

    private fun createCart() =
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
}