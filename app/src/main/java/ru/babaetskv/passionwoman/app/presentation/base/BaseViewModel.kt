package ru.babaetskv.passionwoman.app.presentation.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.terrakok.cicerone.Router
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

abstract class BaseViewModel(
    protected val router: Router
) : ViewModel(), CoroutineScope {
    private val errorHandler = CoroutineExceptionHandler(::onError)

    val loadingLiveData = MutableLiveData(false)

    override val coroutineContext: CoroutineContext = viewModelScope.coroutineContext + Dispatchers.IO + errorHandler

    open fun onResume() = Unit

    open fun onPause() = Unit

    protected open fun onError(context: CoroutineContext, error: Throwable) {
        // TODO: handle errors
    }

    fun onBackPressed() {
        router.exit()
    }

    fun launchWithLoading(callback: suspend () -> Unit) = launch {
        loadingLiveData.postValue(true)
        callback.invoke()
        loadingLiveData.postValue(false)
    }
}
