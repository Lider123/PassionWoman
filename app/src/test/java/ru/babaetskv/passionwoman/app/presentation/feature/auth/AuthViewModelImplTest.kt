package ru.babaetskv.passionwoman.app.presentation.feature.auth

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.test.*
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.*
import ru.babaetskv.passionwoman.app.MainCoroutineRule
import ru.babaetskv.passionwoman.app.analytics.base.AnalyticsHandler
import ru.babaetskv.passionwoman.app.analytics.event.OpenScreenEvent
import ru.babaetskv.passionwoman.app.presentation.base.ViewModelDependencies
import ru.babaetskv.passionwoman.app.presentation.event.EventHub
import ru.babaetskv.passionwoman.app.presentation.event.RouterEvent
import ru.babaetskv.passionwoman.app.utils.NetworkStateChecker
import ru.babaetskv.passionwoman.domain.AppDispatchers
import ru.babaetskv.passionwoman.domain.preferences.AuthPreferences
import ru.babaetskv.passionwoman.domain.usecase.AuthorizeAsGuestUseCase
import ru.babaetskv.passionwoman.domain.usecase.AuthorizeUseCase

@DelicateCoroutinesApi
@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class AuthViewModelImplTest {
    private val argsMock: AuthFragment.Args = mock()
    private val authorizeUseCaseMock: AuthorizeUseCase = mock()
    private val authorizeAsGuestUseCaseMock: AuthorizeAsGuestUseCase = mock()
    private val authPreferencesMock: AuthPreferences = mock()
    private val dependenciesMock: ViewModelDependencies = mock()
    private val analyticsHandlerMock: AnalyticsHandler = mock()
    private val eventHubMock: EventHub = mock()
    private val networkStateCheckerMock: NetworkStateChecker = mock()
    private val dispatchersMock: AppDispatchers = mock()
    private lateinit var viewModel: AuthViewModelImpl

    @get:Rule
    var taskRule = InstantTaskExecutorRule()
    @get:Rule
    var coroutineRule = MainCoroutineRule()

    @Before
    fun before() {
        whenever(dependenciesMock.analyticsHandler) doReturn analyticsHandlerMock
        whenever(dependenciesMock.eventHub) doReturn eventHubMock
        whenever(dependenciesMock.networkStateChecker) doReturn networkStateCheckerMock
        whenever(dependenciesMock.dispatchers) doReturn dispatchersMock
        whenever(eventHubMock.flow) doReturn flow {}
        whenever(networkStateCheckerMock.networkAvailabilityFlowDebounced) doReturn flow {}
        whenever(dispatchersMock.IO) doReturn StandardTestDispatcher()
        viewModel = AuthViewModelImpl(
            args = argsMock,
            authPreferences = authPreferencesMock,
            authorizeAsGuestUseCase = authorizeAsGuestUseCaseMock,
            authorizeUseCase = authorizeUseCaseMock,
            dependencies = dependenciesMock
        )
    }

    @Test
    fun lastPhoneLiveData_hasNoValue_whenInit() {
        val result = viewModel.lastPhoneLiveData.value

        assertNull(result)
    }

    @Test
    fun smsCodeLiveData_hasNoValue_whenInit() {
        val result = viewModel.smsCodeLiveData.value

        assertNull(result)
    }

    @Test
    fun modeLiveData_containsAuthMode_whenInit() {
        val result = viewModel.modeLiveData.value

        assertEquals(AuthViewModel.AuthMode.LOGIN, result)
    }

    @Test
    fun onStart_doesNotLogScreenOpening() {
        viewModel.onStart("ScreenName")

        verify(analyticsHandlerMock, times(0)).log(any<OpenScreenEvent>())
    }

    @Test
    fun onBackPressed_setsLoginMode_whenAuthModeIsSmsConfirm() {
        viewModel.modeLiveData.value = AuthViewModel.AuthMode.SMS_CONFIRM

        viewModel.onBackPressed()
        val result = viewModel.modeLiveData.value

        assertEquals(AuthViewModel.AuthMode.LOGIN, result)
    }

    @Test
    fun onBackPressed_doesNotSendBackPressedEvent_whenAuthModeIsSmsConfirm() = runTest {
        viewModel.modeLiveData.value = AuthViewModel.AuthMode.SMS_CONFIRM
        val routerEventHandlerMock: (RouterEvent) -> Unit = mock()
        viewModel.routerEventBus.onEach(routerEventHandlerMock)
            .launchIn(this)

        viewModel.onBackPressed()

        verify(routerEventHandlerMock, times(0))
            .invoke(any())
    }

    @Test
    fun onBackPressed_sendsBackPressedEvent_whenAuthModeIsLogin() = runTest {
        viewModel.modeLiveData.value = AuthViewModel.AuthMode.LOGIN
        val routerEventHandlerMock: (RouterEvent) -> Unit = mock()
        viewModel.routerEventBus.onEach(routerEventHandlerMock)
            .launchIn(this)

        viewModel.onBackPressed()

        verify(routerEventHandlerMock, times(1))
            .invoke(RouterEvent.GoBack)
    }
}
