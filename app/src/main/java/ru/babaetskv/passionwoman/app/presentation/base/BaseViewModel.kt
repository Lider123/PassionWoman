package ru.babaetskv.passionwoman.app.presentation.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.terrakok.cicerone.Router
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

abstract class BaseViewModel(
    protected val router: Router
) : ViewModel(), CoroutineScope {
    override val coroutineContext: CoroutineContext = viewModelScope.coroutineContext + Dispatchers.IO

    open fun onResume() = Unit

    open fun onPause() = Unit

    fun onBackPressed() {
        router.exit()
    }
}
