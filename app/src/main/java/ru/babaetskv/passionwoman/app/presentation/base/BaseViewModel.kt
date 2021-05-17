package ru.babaetskv.passionwoman.app.presentation.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.terrakok.cicerone.Router
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.babaetskv.passionwoman.app.utils.notifier.Notifier
import kotlin.coroutines.CoroutineContext

abstract class BaseViewModel(
    protected val router: Router
) : ViewModel(), CoroutineScope {
    val loadingLiveData = MutableLiveData(false)

    override val coroutineContext: CoroutineContext = viewModelScope.coroutineContext + Dispatchers.IO

    open fun onResume() = Unit

    open fun onPause() = Unit

    fun onBackPressed() {
        router.exit()
    }

    fun launchWithLoading(callback: suspend () -> Unit) = launch {
        loadingLiveData.postValue(true)
        callback.invoke()
        loadingLiveData.postValue(false)
    }
}
