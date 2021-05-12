package ru.babaetskv.passionwoman.app.presentation.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import ru.babaetskv.passionwoman.app.MainApp
import kotlin.coroutines.CoroutineContext

abstract class BaseViewModel : ViewModel(), KodeinAware, CoroutineScope {
    override val kodein: Kodein by lazy {
        MainApp.instance.kodein
    }
    override val coroutineContext: CoroutineContext = viewModelScope.coroutineContext + Dispatchers.IO

    open fun onResume() = Unit

    open fun onPause() = Unit
}
