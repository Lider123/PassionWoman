package ru.babaetskv.passionwoman.data.gateway

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
import ru.babaetskv.passionwoman.data.api.CommonApi
import ru.babaetskv.passionwoman.data.model.AuthTokenModel
import ru.babaetskv.passionwoman.domain.StringProvider
import ru.babaetskv.passionwoman.domain.exceptions.GatewayException
import java.lang.RuntimeException

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class AuthGatewayImplTest {
    @Mock
    private lateinit var commonApiMock: CommonApi
    @Mock
    private lateinit var stringProvider: StringProvider
    @InjectMocks
    private lateinit var gateway: AuthGatewayImpl

    @Test
    fun authorize_returnsToken(): Unit = runTest {
        val tokenMock = AuthTokenModel("token")
        whenever(commonApiMock.authorize(any())).doReturn(tokenMock)

        val result = gateway.authorize(any())

        assertEquals(tokenMock.token, result)
    }

    @Test
    fun authorize_callsCommonApi(): Unit = runTest {
        val tokenMock = AuthTokenModel("token")
        whenever(commonApiMock.authorize(any())).doReturn(tokenMock)

        gateway.authorize(any())

        verify(commonApiMock, times(1)).authorize(any())
    }

    @Test
    fun authorize_throwsClientException_whenGotNotFoundException(): Unit = runTest {
        val httpExceptionMock = mock<HttpException> {
            whenever(mock.code()).doReturn(404)
        }
        whenever(commonApiMock.authorize(any())).thenThrow(httpExceptionMock)

        runCatching {
            gateway.authorize(any())
        }.onFailure {
            assertTrue(it is GatewayException.Client)
        }.onSuccess {
            fail()
        }
    }

    @Test
    fun authorize_throwsClientException_whenGotBadRequestException(): Unit = runTest {
        val httpExceptionMock = mock<HttpException> {
            whenever(mock.code()).doReturn(400)
        }
        whenever(commonApiMock.authorize(any())).thenThrow(httpExceptionMock)

        runCatching {
            gateway.authorize(any())
        }.onFailure {
            assertTrue(it is GatewayException.Client)
        }.onSuccess {
            fail()
        }
    }

    @Test
    fun authorize_throwsClientException_whenGotForbiddenException(): Unit = runTest {
        val httpExceptionMock = mock<HttpException> {
            whenever(mock.code()).doReturn(403)
        }
        whenever(commonApiMock.authorize(any())).thenThrow(httpExceptionMock)

        runCatching {
            gateway.authorize(any())
        }.onFailure {
            assertTrue(it is GatewayException.Client)
        }.onSuccess {
            fail()
        }
    }

    @Test
    fun authorize_throwsServerException_whenGotInternalServerException(): Unit = runTest {
        val httpExceptionMock = mock<HttpException> {
            whenever(mock.code()).doReturn(500)
        }
        whenever(commonApiMock.authorize(any())).thenThrow(httpExceptionMock)

        runCatching {
            gateway.authorize(any())
        }.onFailure {
            assertTrue(it is GatewayException.Server)
        }.onSuccess {
            fail()
        }
    }

    @Test
    fun authorize_throwsUnknownException_whenGotCommonException(): Unit = runTest {
        whenever(commonApiMock.authorize(any())).thenThrow(RuntimeException())

        runCatching {
            gateway.authorize(any())
        }.onFailure {
            assertTrue(it is GatewayException.Unknown)
        }.onSuccess {
            fail()
        }
    }
}
