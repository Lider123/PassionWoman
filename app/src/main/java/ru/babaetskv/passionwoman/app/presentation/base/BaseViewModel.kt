package ru.babaetskv.passionwoman.app.presentation.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import ru.babaetskv.passionwoman.app.R
import ru.babaetskv.passionwoman.app.analytics.base.AnalyticsHandler
import ru.babaetskv.passionwoman.app.analytics.base.ErrorLogger
import ru.babaetskv.passionwoman.app.analytics.event.OpenScreenEvent
import ru.babaetskv.passionwoman.app.presentation.event.EventHub
import ru.babaetskv.passionwoman.app.presentation.event.InnerEvent
import ru.babaetskv.passionwoman.app.presentation.event.RouterEvent
import ru.babaetskv.passionwoman.app.utils.NetworkStateChecker
import ru.babaetskv.passionwoman.app.utils.notifier.Notifier
import ru.babaetskv.passionwoman.domain.interactor.exception.EmptyDataException
import ru.babaetskv.passionwoman.domain.interactor.exception.NetworkActionException
import ru.babaetskv.passionwoman.domain.interactor.exception.NetworkDataException
import kotlin.coroutines.CoroutineContext

abstract class BaseViewModel<TRouterEvent : RouterEvent>(
    private val dependencies: ViewModelDependencies
) : ViewModel(), IViewModel, CoroutineScope {
    private val routerEventChannel = Channel<RouterEvent>(Channel.RENDEZVOUS)
    private val eventFlow: Flow<InnerEvent> = eventHub.flow
        .onEach(::onEvent)

    protected val notifier: Notifier
        get() = dependencies.notifier
    protected val analyticsHandler: AnalyticsHandler
        get() = dependencies.analyticsHandler
    protected val errorLogger: ErrorLogger
        get() = dependencies.errorLogger
    protected val networkStateChecker: NetworkStateChecker
        get() = dependencies.networkStateChecker
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
    override val routerEventBus: Flow<RouterEvent> = routerEventChannel.receiveAsFlow()
    final override  val eventHub: EventHub
        get() = dependencies.eventHub

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

    override fun onErrorActionPressed() = Unit

    override fun onBackPressed() {
        launch {
            routerEventChannel.send(RouterEvent.GoBack)
        }
    }

    protected open fun onEvent(event: InnerEvent) = Unit

    protected open fun onError(context: CoroutineContext, error: Throwable) {
        loadingLiveData.postValue(false)
        if (error !is EmptyDataException && error.cause !is EmptyDataException) {
            error.printStackTrace()
            errorLogger.logException(error)
        }
        when {
            error is NetworkDataException && !error.dataIsOptional -> {
                (error.cause as? EmptyDataException)?.let {
                    errorLiveData.postValue(it)
                } ?: errorLiveData.postValue(error)
            }
            error is EmptyDataException -> errorLiveData.postValue(error)
            error is NetworkActionException -> {
                notifier.newRequest(this, error.message)
                    .sendError()
            }
            else -> {
                notifier.newRequest(this, R.string.error_unknown)
                    .sendError()
            }
        }
    }

    protected suspend fun navigateTo(event: TRouterEvent) {
        routerEventChannel.send(event)
    }

    protected suspend fun sendEvent(event: InnerEvent) {
        eventHub.post(event)
    }

    protected fun launchWithLoading(callback: suspend () -> Unit) = launch {
        loadingLiveData.postValue(true)
        errorLiveData.postValue(null)
        callback.invoke()
        loadingLiveData.postValue(false)
    }
}
