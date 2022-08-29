package ru.babaetskv.passionwoman.data.gateway

import android.net.Uri
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.*
import retrofit2.HttpException
import ru.babaetskv.passionwoman.data.api.AuthApi
import ru.babaetskv.passionwoman.data.model.OrderModel
import ru.babaetskv.passionwoman.data.model.ProfileModel
import ru.babaetskv.passionwoman.domain.StringProvider
import ru.babaetskv.passionwoman.domain.exceptions.GatewayException
import ru.babaetskv.passionwoman.domain.model.base.Transformable.Companion.transform
import java.lang.RuntimeException

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class ProfileGatewayImplTest {
    @Mock
    private lateinit var authApiMock: AuthApi
    @Mock
    private lateinit var stringProvider: StringProvider
    @InjectMocks
    private lateinit var gateway: ProfileGatewayImpl

    private fun createProfileMock() =
        ProfileModel(
            id = 1,
            name = "John",
            surname = "Doe",
            phone = "0000000000",
            avatar = null
        )

    @Test
    fun getProfile_returnsProfile() = runTest {
        val profileMock = createProfileMock()
        whenever(authApiMock.getProfile()).doReturn(profileMock)

        val result = gateway.getProfile()

        assertEquals(profileMock, result)
    }

    @Test
    fun getProfile_callsAuthApi() = runTest {
        gateway.getProfile()

        verify(authApiMock, times(1)).getProfile()
    }

    @Test
    fun getProfile_throwsUnauthorizedException_whenGotUnauthorizedException(): Unit = runTest {
        val httpExceptionMock = mock<HttpException> {
            whenever(mock.code()).doReturn(401)
        }
        whenever(authApiMock.getProfile()).thenThrow(httpExceptionMock)

        try {
            gateway.getProfile()
            fail()
        } catch(e: Exception) {
            assertTrue(e is GatewayException.Unauthorized)
        }
    }

    @Test
    fun getProfile_throwsClientException_whenGotNotFoundException(): Unit = runTest {
        val httpExceptionMock = mock<HttpException> {
            whenever(mock.code()).doReturn(404)
        }
        whenever(authApiMock.getProfile()).thenThrow(httpExceptionMock)

        try {
            gateway.getProfile()
            fail()
        } catch (e: Exception) {
            assertTrue(e is GatewayException.Client)
        }
    }

    @Test
    fun getProfile_throwsClientException_whenGotBadRequestException(): Unit = runTest {
        val httpExceptionMock = mock<HttpException> {
            whenever(mock.code()).doReturn(400)
        }
        whenever(authApiMock.getProfile()).thenThrow(httpExceptionMock)

        try {
            gateway.getProfile()
            fail()
        } catch (e: Exception) {
            assertTrue(e is GatewayException.Client)
        }
    }

    @Test
    fun getProfile_throwsClientException_whenGotForbiddenException(): Unit = runTest {
        val httpExceptionMock = mock<HttpException> {
            whenever(mock.code()).doReturn(403)
        }
        whenever(authApiMock.getProfile()).thenThrow(httpExceptionMock)

        try {
            gateway.getProfile()
            fail()
        } catch (e: Exception) {
            assertTrue(e is GatewayException.Client)
        }
    }

    @Test
    fun getProfile_throwsServerException_whenGotInternalServerException(): Unit = runTest {
        val httpExceptionMock = mock<HttpException> {
            whenever(mock.code()).doReturn(500)
        }
        whenever(authApiMock.getProfile()).thenThrow(httpExceptionMock)

        try {
            gateway.getProfile()
            fail()
        } catch (e: Exception) {
            assertTrue(e is GatewayException.Server)
        }
    }

    @Test
    fun getProfile_throwsUnknownException_whenGotCommonException(): Unit = runTest {
        whenever(authApiMock.getProfile()).thenThrow(RuntimeException())

        try {
            gateway.getProfile()
            fail()
        } catch (e: Exception) {
            assertTrue(e is GatewayException.Unknown)
        }
    }

    @Test
    fun updateProfile_callsAuthApi() = runTest {
        val profileMock = createProfileMock()

        gateway.updateProfile(profileMock.transform())

        verify(authApiMock, times(1)).updateProfile(profileMock)
    }

    @Test
    fun updateProfile_throwsUnauthorizedException_whenGotUnauthorizedException(): Unit = runTest {
        val httpExceptionMock = mock<HttpException> {
            whenever(mock.code()).doReturn(401)
        }
        whenever(authApiMock.updateProfile(any())).thenThrow(httpExceptionMock)

        try {
            gateway.updateProfile(createProfileMock().transform())
            fail()
        } catch (e: Exception) {
            assertTrue(e is GatewayException.Unauthorized)
        }
    }

    @Test
    fun updateProfile_throwsClientException_whenGotNotFoundException(): Unit = runTest {
        val httpExceptionMock = mock<HttpException> {
            whenever(mock.code()).doReturn(404)
        }
        whenever(authApiMock.updateProfile(any())).thenThrow(httpExceptionMock)

        try {
            gateway.updateProfile(createProfileMock().transform())
            fail()
        } catch (e: Exception) {
            assertTrue(e is GatewayException.Client)
        }
    }

    @Test
    fun updateProfile_throwsClientException_whenGotBadRequestException(): Unit = runTest {
        val httpExceptionMock = mock<HttpException> {
            whenever(mock.code()).doReturn(400)
        }
        whenever(authApiMock.updateProfile(any())).thenThrow(httpExceptionMock)

        try {
            gateway.updateProfile(createProfileMock().transform())
            fail()
        } catch (e: Exception) {
            assertTrue(e is GatewayException.Client)
        }
    }

    @Test
    fun updateProfile_throwsClientException_whenGotForbiddenException(): Unit = runTest {
        val httpExceptionMock = mock<HttpException> {
            whenever(mock.code()).doReturn(403)
        }
        whenever(authApiMock.updateProfile(any())).thenThrow(httpExceptionMock)

        try {
            gateway.updateProfile(createProfileMock().transform())
            fail()
        } catch (e: Exception) {
            assertTrue(e is GatewayException.Client)
        }
    }

    @Test
    fun updateProfile_throwsServerException_whenGotInternalServerException(): Unit = runTest {
        val httpExceptionMock = mock<HttpException> {
            whenever(mock.code()).doReturn(500)
        }
        whenever(authApiMock.updateProfile(any())).thenThrow(httpExceptionMock)

        try {
            gateway.updateProfile(createProfileMock().transform())
            fail()
        } catch (e: Exception) {
            assertTrue(e is GatewayException.Server)
        }
    }

    @Test
    fun updateProfile_throwsUnknownException_whenGotCommonException(): Unit = runTest {
        whenever(authApiMock.updateProfile(any())).thenThrow(RuntimeException())

        try {
            gateway.updateProfile(createProfileMock().transform())
            fail()
        } catch (e: Exception) {
            assertTrue(e is GatewayException.Unknown)
        }
    }

    @Test
    fun updateAvatar_callsAuthApi_whenUriIsCorrect() = runTest {
        val uriMock = mock<Uri> {
            whenever(mock.toString()).doReturn("file://sample_file")
        }

        gateway.updateAvatar(uriMock)

        verify(authApiMock, times(1)).uploadAvatar(any())
    }

    @Test
    fun updateAvatar_doesNotUpdateApiAvatar_whenUriIsEmpty() = runTest {
        val uriMock = mock<Uri> {
            whenever(mock.toString()).doReturn("")
        }

        gateway.updateAvatar(uriMock)

        verify(authApiMock, times(0)).uploadAvatar(any())
    }

    @Test
    fun updateAvatar_doesNotUpdateApiAvatar_whenUriIsNotFile() = runTest {
        val uriMock = mock<Uri> {
            whenever(mock.toString()).doReturn("sample_file")
        }

        gateway.updateAvatar(uriMock)

        verify(authApiMock, times(0)).uploadAvatar(any())
    }

    @Test
    fun updateAvatar_throwsUnauthorizedException_whenGotUnauthorizedException(): Unit = runTest {
        val httpExceptionMock = mock<HttpException> {
            whenever(mock.code()).doReturn(401)
        }
        whenever(authApiMock.uploadAvatar(any())).thenThrow(httpExceptionMock)
        val uriMock = mock<Uri> {
            whenever(mock.toString()).doReturn("file://sample_file")
        }

        try {
            gateway.updateAvatar(uriMock)
            fail()
        } catch (e: Exception) {
            assertTrue(e is GatewayException.Unauthorized)
        }
    }

    @Test
    fun updateAvatar_throwsClientException_whenGotNotFoundException(): Unit = runTest {
        val httpExceptionMock = mock<HttpException> {
            whenever(mock.code()).doReturn(404)
        }
        whenever(authApiMock.uploadAvatar(any())).thenThrow(httpExceptionMock)
        val uriMock = mock<Uri> {
            whenever(mock.toString()).doReturn("file://sample_file")
        }

        try {
            gateway.updateAvatar(uriMock)
            fail()
        } catch (e: Exception) {
            assertTrue(e is GatewayException.Client)
        }
    }

    @Test
    fun updateAvatar_throwsClientException_whenGotBadRequestException(): Unit = runTest {
        val httpExceptionMock = mock<HttpException> {
            whenever(mock.code()).doReturn(400)
        }
        whenever(authApiMock.uploadAvatar(any())).thenThrow(httpExceptionMock)
        val uriMock = mock<Uri> {
            whenever(mock.toString()).doReturn("file://sample_file")
        }

        try {
            gateway.updateAvatar(uriMock)
            fail()
        } catch (e: Exception) {
            assertTrue(e is GatewayException.Client)
        }
    }

    @Test
    fun updateAvatar_throwsClientException_whenGotForbiddenException(): Unit = runTest {
        val httpExceptionMock = mock<HttpException> {
            whenever(mock.code()).doReturn(403)
        }
        whenever(authApiMock.uploadAvatar(any())).thenThrow(httpExceptionMock)
        val uriMock = mock<Uri> {
            whenever(mock.toString()).doReturn("file://sample_file")
        }

        try {
            gateway.updateAvatar(uriMock)
            fail()
        } catch (e: Exception) {
            assertTrue(e is GatewayException.Client)
        }
    }

    @Test
    fun updateAvatar_throwsServerException_whenGotInternalServerException(): Unit = runTest {
        val httpExceptionMock = mock<HttpException> {
            whenever(mock.code()).doReturn(500)
        }
        whenever(authApiMock.uploadAvatar(any())).thenThrow(httpExceptionMock)
        val uriMock = mock<Uri> {
            whenever(mock.toString()).doReturn("file://sample_file")
        }

        try {
            gateway.updateAvatar(uriMock)
            fail()
        } catch (e: Exception) {
            assertTrue(e is GatewayException.Server)
        }
    }

    @Test
    fun updateAvatar_throwsUnknownException_whenGotCommonException(): Unit = runTest {
        whenever(authApiMock.uploadAvatar(any())).thenThrow(RuntimeException())
        val uriMock = mock<Uri> {
            whenever(mock.toString()).doReturn("file://sample_file")
        }

        try {
            gateway.updateAvatar(uriMock)
            fail()
        } catch (e: Exception) {
            assertTrue(e is GatewayException.Unknown)
        }
    }

    @Test
    fun getOrders_returnsOrders() = runTest {
        val orders = listOf(
            OrderModel(
                id = 1,
                createdAt = "order_1_created_at",
                cartItems = emptyList(),
                status = "order_1_status"
            ),
            OrderModel(
                id = 2,
                createdAt = "order_2_created_at",
                cartItems = emptyList(),
                status = "order_2_status"
            )
        )
        whenever(authApiMock.getOrders()).doReturn(orders)

        val result = gateway.getOrders()

        assertEquals(orders, result)
    }

    @Test
    fun getOrders_callsAuthApi() = runTest {
        gateway.getOrders()

        verify(authApiMock, times(1)).getOrders()
    }

    @Test
    fun getOrders_throwsUnauthorizedException_whenGotUnauthorizedException(): Unit = runTest {
        val httpExceptionMock = mock<HttpException> {
            whenever(mock.code()).doReturn(401)
        }
        whenever(authApiMock.getOrders()).thenThrow(httpExceptionMock)

        try {
            gateway.getOrders()
            fail()
        } catch(e: Exception) {
            assertTrue(e is GatewayException.Unauthorized)
        }
    }

    @Test
    fun getOrders_throwsClientException_whenGotNotFoundException(): Unit = runTest {
        val httpExceptionMock = mock<HttpException> {
            whenever(mock.code()).doReturn(404)
        }
        whenever(authApiMock.getOrders()).thenThrow(httpExceptionMock)

        try {
            gateway.getOrders()
            fail()
        } catch (e: Exception) {
            assertTrue(e is GatewayException.Client)
        }
    }

    @Test
    fun getOrders_throwsClientException_whenGotBadRequestException(): Unit = runTest {
        val httpExceptionMock = mock<HttpException> {
            whenever(mock.code()).doReturn(400)
        }
        whenever(authApiMock.getOrders()).thenThrow(httpExceptionMock)

        try {
            gateway.getOrders()
            fail()
        } catch (e: Exception) {
            assertTrue(e is GatewayException.Client)
        }
    }

    @Test
    fun getOrders_throwsClientException_whenGotForbiddenException(): Unit = runTest {
        val httpExceptionMock = mock<HttpException> {
            whenever(mock.code()).doReturn(403)
        }
        whenever(authApiMock.getOrders()).thenThrow(httpExceptionMock)

        try {
            gateway.getOrders()
            fail()
        } catch (e: Exception) {
            assertTrue(e is GatewayException.Client)
        }
    }

    @Test
    fun getOrders_throwsServerException_whenGotInternalServerException(): Unit = runTest {
        val httpExceptionMock = mock<HttpException> {
            whenever(mock.code()).doReturn(500)
        }
        whenever(authApiMock.getOrders()).thenThrow(httpExceptionMock)

        try {
            gateway.getOrders()
            fail()
        } catch (e: Exception) {
            assertTrue(e is GatewayException.Server)
        }
    }

    @Test
    fun getOrders_throwsUnknownException_whenGotCommonException(): Unit = runTest {
        whenever(authApiMock.getOrders()).thenThrow(RuntimeException())

        try {
            gateway.getOrders()
            fail()
        } catch (e: Exception) {
            assertTrue(e is GatewayException.Unknown)
        }
    }

    // TODO: add tests
}
