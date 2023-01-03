package ru.babaetskv.passionwoman.app.presentation

import android.content.Intent
import ru.babaetskv.passionwoman.app.presentation.base.IViewModel
import ru.babaetskv.passionwoman.app.presentation.event.Event
import ru.babaetskv.passionwoman.app.utils.notifier.AlertMessage

interface MainViewModel : IViewModel {
    val appIsReady: Boolean

    fun handleIntent(intent: Intent, startApp: Boolean)

    data class ShowAlertMessageEvent(
        val message: AlertMessage
    ) : Event
}
