package ru.babaetskv.passionwoman.data.gateway.exception

import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import retrofit2.HttpException
import ru.babaetskv.passionwoman.domain.StringProvider
import ru.babaetskv.passionwoman.domain.exceptions.GatewayException
import java.lang.RuntimeException
import kotlin.random.Random

@RunWith(MockitoJUnitRunner::class)
class GatewayExceptionProviderImplTest {
    @Mock
    lateinit var stringProvider: StringProvider
    @InjectMocks
    lateinit var provider: GatewayExceptionProviderImpl

    @Test
    fun getGatewayException_returnsUnknownException_ifExceptionIsNotHttpException() {
        val result = provider.getGatewayException(RuntimeException())

        assertTrue(result is GatewayException.Unknown)
    }

    @Test
    fun getGatewayException_returnsUnauthorizedException_ifExceptionIsHttpExceptionAndCodeIs401() {
        val exceptionMock: HttpException = mock {
            whenever(mock.code()) doReturn 401
        }
        val result = provider.getGatewayException(exceptionMock)

        assertTrue(result is GatewayException.Unauthorized)
    }

    @Test
    fun getGatewayException_returnsClientException_ifExceptionIsHttpExceptionAndCodeStartsWith4() {
        val exceptionMock: HttpException = mock {
            whenever(mock.code()) doReturn Random.nextInt(0, 100) + 400
        }
        val result = provider.getGatewayException(exceptionMock)

        assertTrue(result is GatewayException.Client)
    }

    @Test
    fun getGatewayException_returnsServerException_ifExceptionIsHttpExceptionAndCodeStartsWith5() {
        val exceptionMock: HttpException = mock {
            whenever(mock.code()) doReturn Random.nextInt(0, 100) + 500
        }
        val result = provider.getGatewayException(exceptionMock)

        assertTrue(result is GatewayException.Server)
    }

    @Test
    fun getGatewayException_returnsUnknownException_ifExceptionIsHttpExceptionAndCodeIsUnknown() {
        val exceptionMock: HttpException = mock {
            whenever(mock.code()) doReturn Random.nextInt(100, 400)
        }
        val result = provider.getGatewayException(exceptionMock)

        assertTrue(result is GatewayException.Unknown)
    }
}
