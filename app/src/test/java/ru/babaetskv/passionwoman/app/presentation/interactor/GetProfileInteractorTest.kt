package ru.babaetskv.passionwoman.app.presentation.interactor

import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.*
import ru.babaetskv.passionwoman.domain.StringProvider
import ru.babaetskv.passionwoman.domain.gateway.AuthGateway
import ru.babaetskv.passionwoman.domain.model.Profile
import ru.babaetskv.passionwoman.domain.model.base.Transformable
import ru.babaetskv.passionwoman.domain.usecase.GetProfileUseCase
import ru.babaetskv.passionwoman.domain.utils.transform

@RunWith(MockitoJUnitRunner::class)
class GetProfileInteractorTest {
    @Mock
    private lateinit var authGatewayMock: AuthGateway
    @Mock
    private lateinit var stringProviderMock: StringProvider
    @InjectMocks
    private lateinit var interactor: GetProfileInteractor

    private val profileMock = Profile(
        id = "id",
        name = "John",
        surname = "Doe",
        phone = "",
        avatar = null
    )

    @Before
    fun before(): Unit = runBlocking {
        val transformableMock: Transformable<Unit, Profile> = mock {
            whenever(mock.transform()) doReturn profileMock
        }
        whenever(authGatewayMock.getProfile()) doReturn transformableMock
    }

    @Test
    fun execute_invokesAuthGateway(): Unit = runBlocking {
        interactor.execute()
        verify(authGatewayMock, times(1)).getProfile()
    }

    @Test
    fun execute_returnsProfile() = runBlocking {
        val actual = interactor.execute()
        assertEquals(profileMock, actual)
    }

    @Test
    fun execute_throwsGetProfileException_whenCatchesException() = runBlocking {
        whenever(authGatewayMock.getProfile()).thenThrow(RuntimeException())
        try {
            interactor.execute()
            fail()
        } catch (e: Exception) {
            assertTrue(e is GetProfileUseCase.GetProfileException)
        }
    }
}
