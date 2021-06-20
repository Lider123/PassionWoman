package ru.babaetskv.passionwoman.app.presentation.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import ru.babaetskv.passionwoman.app.R
import ru.babaetskv.passionwoman.app.utils.notifier.Notifier
import ru.babaetskv.passionwoman.domain.interactor.exception.NetworkActionException
import ru.babaetskv.passionwoman.domain.interactor.exception.NetworkDataException
import kotlin.coroutines.CoroutineContext

abstract class BaseViewModel<TRouterEvent : RouterEvent>(
    protected val notifier: Notifier
) : ViewModel(), CoroutineScope {
    private val routerEventChannel = Channel<RouterEvent>(Channel.RENDEZVOUS)

    val loadingLiveData = MutableLiveData(false)
    val errorLiveData = MutableLiveData<Exception?>(null)
    val routerEventBus: Flow<RouterEvent> = routerEventChannel.receiveAsFlow()

    override val coroutineContext: CoroutineContext =
        viewModelScope.coroutineContext + Dispatchers.IO + CoroutineExceptionHandler(::onError)

    open fun onStart() = Unit

    open fun onResume() = Unit

    open fun onPause() = Unit

    open fun onStop() = Unit

    open fun onErrorActionPressed() = Unit

    open fun onBackPressed() {
        launch {
            routerEventChannel.send(RouterEvent.GoBack)
        }
    }

    protected open fun onError(context: CoroutineContext, error: Throwable) {
        loadingLiveData.postValue(false)
        error.printStackTrace()
        when (error) {
            is NetworkDataException -> {
                errorLiveData.postValue(error)
            }
            is NetworkActionException -> {
                val request = error.message.let {
                    notifier.newRequest(this, it)
                }
                request.sendError()
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

    fun launchWithLoading(callback: suspend () -> Unit) = launch {
        loadingLiveData.postValue(true)
        errorLiveData.postValue(null)
        callback.invoke()
        loadingLiveData.postValue(false)
    }
}
