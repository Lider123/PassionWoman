package ru.babaetskv.passionwoman.app.presentation.interactor

import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.*
import ru.babaetskv.passionwoman.domain.StringProvider
import ru.babaetskv.passionwoman.domain.preferences.AuthPreferences
import ru.babaetskv.passionwoman.domain.usecase.AuthorizeAsGuestUseCase

@RunWith(MockitoJUnitRunner::class)
class AuthorizeAsGuestInteractorTest {
    @Mock
    private lateinit var authPrefsMock: AuthPreferences
    @Mock
    private lateinit var stringProviderMock: StringProvider
    @InjectMocks
    private lateinit var interactor: AuthorizeAsGuestInteractor

    @Test
    fun execute_setsAuthTypeWithGuest_whenIsSuccess() = runBlocking {
        interactor.execute()

        verify(authPrefsMock, times(1)).authType = AuthPreferences.AuthType.GUEST
    }

    @Test
    fun execute_throwsAuthorizeAsGuestException_whenCatchesException() = runBlocking {
        doThrow(RuntimeException()).whenever(authPrefsMock).authType = any()
        whenever(stringProviderMock.AUTHORIZE_AS_GUEST_ERROR).thenReturn("error")

        try {
            interactor.execute()
            fail()
        } catch (e: Exception) {
            assertTrue(e is AuthorizeAsGuestUseCase.AuthorizeAsGuestException)
        }
    }
}
