package ru.babaetskv.passionwoman.app.presentation.base

import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow
import ru.babaetskv.passionwoman.app.presentation.event.RouterEvent

interface IViewModel {
    val loadingLiveData: LiveData<Boolean>
    val errorLiveData: LiveData<Exception?>
    val routerEventBus: Flow<RouterEvent>

    fun onErrorActionPressed()
    fun onStart(screenName: String)
    fun onResume()
    fun onPause()
    fun onStop()
    fun onBackPressed()
}
