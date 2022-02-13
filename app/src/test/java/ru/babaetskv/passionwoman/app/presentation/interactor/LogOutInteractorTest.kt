package ru.babaetskv.passionwoman.app.presentation.interactor

import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.*
import ru.babaetskv.passionwoman.domain.StringProvider
import ru.babaetskv.passionwoman.domain.preferences.AuthPreferences
import ru.babaetskv.passionwoman.domain.preferences.FavoritesPreferences
import ru.babaetskv.passionwoman.domain.usecase.LogOutUseCase

@RunWith(MockitoJUnitRunner::class)
class LogOutInteractorTest {
    @Mock
    private lateinit var authPrefsMock: AuthPreferences
    @Mock
    private lateinit var favoritesPrefsMock: FavoritesPreferences
    @Mock
    private lateinit var stringProviderMock: StringProvider
    @InjectMocks
    private lateinit var interactor: LogOutInteractor

    @Before
    fun beforeTest() {
        whenever(stringProviderMock.LOG_OUT_ERROR).doReturn("")
    }

    @Test
    fun execute_resetsAuthPrefs() = runBlocking {
        interactor.execute()
        verify(authPrefsMock, times(1)).reset()
    }

    @Test
    fun execute_resetsFavoritesPrefs() = runBlocking {
        interactor.execute()
        verify(favoritesPrefsMock, times(1)).reset()
    }

    @Test
    fun execute_throwsLogOutException_whenCatchesException() = runBlocking {
        whenever(authPrefsMock.reset()).thenThrow(RuntimeException())
        try {
            interactor.execute()
            fail()
        } catch (e: Exception) {
            assertTrue(e is LogOutUseCase.LogOutException)
        }
    }
}
