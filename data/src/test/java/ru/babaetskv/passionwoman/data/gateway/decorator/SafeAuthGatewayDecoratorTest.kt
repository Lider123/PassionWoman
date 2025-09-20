package ru.babaetskv.passionwoman.data.gateway.decorator

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.*
import ru.babaetskv.passionwoman.domain.exceptions.GatewayException
import ru.babaetskv.passionwoman.domain.gateway.AuthGateway
import ru.babaetskv.passionwoman.domain.gateway.exception.GatewayExceptionProvider

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class SafeAuthGatewayDecoratorTest {
    @Mock
    lateinit var gatewayMock: AuthGateway
    @Mock
    lateinit var exceptionProviderMock: GatewayExceptionProvider
    @InjectMocks
    lateinit var decorator: SafeAuthGatewayDecorator

    @Test
    fun authorize_callsExceptionProvider_ifThrowsError() = runTest {
        val exception = RuntimeException()
        whenever(gatewayMock.authorize(any())) doThrow exception
        whenever(exceptionProviderMock.getGatewayException(exception)) doReturn mock()

        try {
            decorator.authorize("token")
            fail()
        } catch (e: GatewayException) {
            verify(exceptionProviderMock, times(1))
                .getGatewayException(exception)
        }
    }

    @Test
    fun authorize_throwsGatewayException_ifThrowsError() = runTest {
        val exception = RuntimeException()
        val gatewayExceptionMock: GatewayException = mock()
        whenever(gatewayMock.authorize(any())) doThrow exception
        whenever(exceptionProviderMock.getGatewayException(exception)) doReturn gatewayExceptionMock

        try {
            decorator.authorize("token")
            fail()
        } catch (e: GatewayException) {
            assertEquals(gatewayExceptionMock, e)
        }
    }

    @Test
    fun authorize_callsGateway() = runTest {
        decorator.authorize("token")

        verify(gatewayMock, times(1))
            .authorize("token")
    }

    @Test
    fun authorize_returnsAuthToken_ifDoesNotThrowError() = runTest {
        val authToken = "authToken"
        whenever(gatewayMock.authorize("token")) doReturn authToken

        val result = decorator.authorize("token")

        assertEquals(authToken, result)
    }
}