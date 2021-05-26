package ru.babaetskv.passionwoman.app.presentation.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.terrakok.cicerone.Router
import kotlinx.coroutines.*
import ru.babaetskv.passionwoman.app.R
import ru.babaetskv.passionwoman.app.utils.notifier.Notifier
import ru.babaetskv.passionwoman.domain.interactor.exception.NetworkActionException
import ru.babaetskv.passionwoman.domain.interactor.exception.NetworkDataException
import kotlin.coroutines.CoroutineContext

abstract class BaseViewModel(
    protected val notifier: Notifier,
    protected val router: Router
) : ViewModel(), CoroutineScope {
    val loadingLiveData = MutableLiveData(false)
    val errorLiveData = MutableLiveData<Exception?>(null)

    override val coroutineContext: CoroutineContext =
        viewModelScope.coroutineContext + Dispatchers.IO + CoroutineExceptionHandler(::onError)

    open fun onResume() = Unit

    open fun onPause() = Unit

    open fun onErrorActionPressed() = Unit

    protected open fun onError(context: CoroutineContext, error: Throwable) {
        loadingLiveData.postValue(false)
        error.printStackTrace()
        when (error) {
            is NetworkDataException -> {
                errorLiveData.postValue(error)
            }
            is NetworkActionException -> {
                val request = error.message?.let {
                    notifier.newRequest(this, it)
                } ?: notifier.newRequest(this, R.string.error_message)
                request.sendError()
            }
            else -> {
                notifier.newRequest(this, R.string.error_message)
                    .sendError()
            }
        }
    }

    open fun onBackPressed() {
        router.exit()
    }

    fun launchWithLoading(callback: suspend () -> Unit) = launch {
        loadingLiveData.postValue(true)
        errorLiveData.postValue(null)
        callback.invoke()
        loadingLiveData.postValue(false)
    }
}
