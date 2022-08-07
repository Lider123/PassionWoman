package ru.babaetskv.passionwoman.data.gateway

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.*
import ru.babaetskv.passionwoman.data.api.CommonApi
import ru.babaetskv.passionwoman.data.model.AuthTokenModel

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class AuthGatewayImplTest {
    @Mock
    private lateinit var commonApiMock: CommonApi
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
        gateway.authorize(any())

        verify(commonApiMock, times(1)).authorize(any())
    }
}
