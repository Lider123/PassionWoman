package ru.babaetskv.passionwoman.app.presentation.base

import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow
import ru.babaetskv.passionwoman.app.presentation.event.Event

interface IViewModel {
    val loadingLiveData: LiveData<Boolean>
    val errorLiveData: LiveData<Exception?>
    val eventFlow: Flow<Event>

    fun onErrorActionPressed(exception: Exception)
    fun onStart(screenName: String)
    fun onResume()
    fun onPause()
    fun onStop()
    fun onBackPressed()
}
