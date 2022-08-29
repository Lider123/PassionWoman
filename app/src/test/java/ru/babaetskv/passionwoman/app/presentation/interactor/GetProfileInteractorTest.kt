package ru.babaetskv.passionwoman.app.presentation.interactor

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.*
import ru.babaetskv.passionwoman.domain.StringProvider
import ru.babaetskv.passionwoman.domain.gateway.ProfileGateway
import ru.babaetskv.passionwoman.domain.model.Profile
import ru.babaetskv.passionwoman.domain.model.base.Transformable
import ru.babaetskv.passionwoman.domain.model.base.Transformable.Companion.transform
import ru.babaetskv.passionwoman.domain.usecase.GetProfileUseCase

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class GetProfileInteractorTest {
    @Mock
    private lateinit var profileGatewayMock: ProfileGateway
    @Mock
    private lateinit var stringProviderMock: StringProvider
    @InjectMocks
    private lateinit var interactor: GetProfileInteractor

    private val profileMock = Profile(
        id = 1,
        name = "John",
        surname = "Doe",
        phone = "",
        avatar = null
    )

    @Before
    fun before(): Unit = runTest {
        val transformableMock: Transformable<Unit, Profile> = mock {
            whenever(mock.transform()) doReturn profileMock
        }
        whenever(profileGatewayMock.getProfile()) doReturn transformableMock
        whenever(stringProviderMock.GET_PROFILE_ERROR) doReturn "error"
    }

    @Test
    fun execute_callsProfileGateway(): Unit = runTest {
        interactor.execute()

        verify(profileGatewayMock, times(1)).getProfile()
    }

    @Test
    fun execute_returnsProfile() = runTest {
        val result = interactor.execute()

        assertEquals(profileMock, result)
    }

    @Test
    fun execute_throwsGetProfileException_whenCatchesException() = runTest {
        whenever(profileGatewayMock.getProfile()).thenThrow(RuntimeException())

        runCatching {
            interactor.execute()
        }.onFailure {
            assertTrue(it is GetProfileUseCase.GetProfileException)
        }.onSuccess {
            fail()
        }
    }
}
