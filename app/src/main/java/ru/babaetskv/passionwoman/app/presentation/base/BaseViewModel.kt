package ru.babaetskv.passionwoman.app.presentation.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import ru.babaetskv.passionwoman.app.R
import ru.babaetskv.passionwoman.app.analytics.base.AnalyticsHandler
import ru.babaetskv.passionwoman.app.analytics.base.ErrorLogger
import ru.babaetskv.passionwoman.app.analytics.event.OpenScreenEvent
import ru.babaetskv.passionwoman.app.navigation.AppRouter
import ru.babaetskv.passionwoman.app.navigation.Screens
import ru.babaetskv.passionwoman.app.presentation.event.Event
import ru.babaetskv.passionwoman.app.presentation.event.EventHub
import ru.babaetskv.passionwoman.app.utils.NetworkStateChecker
import ru.babaetskv.passionwoman.app.utils.notifier.Notifier
import ru.babaetskv.passionwoman.domain.exceptions.GatewayException
import ru.babaetskv.passionwoman.domain.exceptions.UseCaseException
import kotlin.coroutines.CoroutineContext

abstract class BaseViewModel(
    private val dependencies: ViewModelDependencies
) : ViewModel(), IViewModel, CoroutineScope {
    private val eventHub: EventHub
        get() = dependencies.eventHub

    protected val notifier: Notifier
        get() = dependencies.notifier
    protected val analyticsHandler: AnalyticsHandler
        get() = dependencies.analyticsHandler
    protected val errorLogger: ErrorLogger
        get() = dependencies.errorLogger
    protected val networkStateChecker: NetworkStateChecker
        get() = dependencies.networkStateChecker
    protected val router: AppRouter
        get() = dependencies.router
    protected val isPortraitModeOnly: Boolean
        get() = dependencies.config.isPortraitModeOnly
    protected open val logScreenOpening: Boolean = true

    val networkAvailabilityFlow: Flow<Boolean> =
        networkStateChecker.networkAvailabilityFlowDebounced.onEach { isConnected ->
            if (!isConnected) {
                notifier.newRequest(this, R.string.error_network_unavailable)
                    .withIcon(R.drawable.ic_no_internet)
                    .isImportant(false)
                    .sendError()
            }
        }

    override val coroutineContext: CoroutineContext =
        viewModelScope.coroutineContext + Dispatchers.IO + CoroutineExceptionHandler(::onError)
    override val loadingLiveData = MutableLiveData(false)
    override val errorLiveData = MutableLiveData<Exception?>(null)
    final override val eventFlow: Flow<Event> = eventHub.flow
        .onEach(::onEvent)

    init {
        eventFlow.launchIn(viewModelScope)
        networkAvailabilityFlow.launchIn(viewModelScope)
    }

    override fun onStart(screenName: String) {
        if (logScreenOpening) analyticsHandler.log(OpenScreenEvent(screenName))
    }

    override fun onResume() = Unit

    override fun onPause() = Unit

    override fun onStop() = Unit

    override fun onErrorActionPressed(exception: Exception) {
        if (exception is GatewayException.Unauthorized) {
            router.navigateTo(Screens.auth(false))
        }
    }

    override fun onBackPressed() {
        router.exit()
    }

    protected open fun onEvent(event: Event) = Unit

    protected open fun onError(context: CoroutineContext, error: Throwable) {
        loadingLiveData.postValue(false)
        if ((error as? UseCaseException)?.isCritical != false) {
            error.printStackTrace()
            errorLogger.logException(error)
        }
        if (error !is UseCaseException) {
            onUnknownException()
            return
        }

        when {
            handleUnauthorizedException(error) -> return
            error is UseCaseException.Data -> onDataException(error)
            error is UseCaseException.EmptyData -> onEmptyDataException(error)
            error is UseCaseException.Action -> onNetworkActionException(error)
            else -> onUnknownException()
        }
    }

    private fun onEmptyDataException(exception: UseCaseException.EmptyData) {
        errorLiveData.postValue(exception)
    }

    private fun onDataException(exception: UseCaseException.Data) {
        errorLiveData.postValue(exception)
    }

    private fun onUnknownException() {
        notifier.newRequest(this, R.string.error_unknown)
            .sendError()
    }

    private fun onNetworkActionException(exception: UseCaseException.Action) {
        notifier.newRequest(this, exception.message)
            .sendError()
    }

    private fun handleUnauthorizedException(exception: UseCaseException): Boolean {
        val cause = exception.cause as? GatewayException.Unauthorized ?: return false

        when (exception) {
            is UseCaseException.Data -> errorLiveData.postValue(cause)
            is UseCaseException.Action -> {
                notifier.newRequest(this, cause.message)
                    .sendError()
            }
            is UseCaseException.EmptyData -> Unit
        }
        return true
    }

    protected suspend fun sendEvent(event: Event) {
        eventHub.post(event)
    }

    protected fun launchWithLoading(callback: suspend () -> Unit) = launch {
        loadingLiveData.postValue(true)
        errorLiveData.postValue(null)
        callback.invoke()
        loadingLiveData.postValue(false)
    }
}
