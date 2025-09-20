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
        val tokenModel = AuthTokenModel("token")
        whenever(commonApiMock.authorize(any())) doReturn tokenModel

        val result = gateway.authorize("accessToken")

        assertEquals(tokenModel.token, result)
    }

    @Test
    fun authorize_callsCommonApi(): Unit = runTest {
        val tokenModel = AuthTokenModel("token")
        whenever(commonApiMock.authorize(any())) doReturn tokenModel

        gateway.authorize("accessToken")

        verify(commonApiMock, times(1))
            .authorize(any())
    }
}
