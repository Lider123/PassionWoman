package ru.babaetskv.passionwoman.app.presentation

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import ru.babaetskv.passionwoman.app.presentation.base.BaseViewModel
import ru.babaetskv.passionwoman.app.presentation.base.RouterEvent
import ru.babaetskv.passionwoman.app.utils.notifier.Message
import ru.babaetskv.passionwoman.app.utils.notifier.Notifier

class MainViewModel(notifier: Notifier) : BaseViewModel<MainViewModel.Router>(notifier) {
    private var alertChannel: ReceiveChannel<Message>? = null
    private val eventChannel = Channel<Event>(Channel.RENDEZVOUS)

    val eventBus: Flow<Event> = eventChannel.consumeAsFlow()

    init {
        launch {
            navigateTo(Router.SplashScreen)
        }
    }

    override fun onStart() {
        super.onStart()
        subscribeOnAlerts()
    }

    override fun onStop() {
        unsubscribeFromAlerts()
        super.onStop()
    }

    private fun subscribeOnAlerts() {
        alertChannel = notifier.subscribe()
        launch {
            alertChannel!!.consumeAsFlow().collect(::onNextAlertMessage)
        }
    }

    private fun unsubscribeFromAlerts() {
        alertChannel?.cancel()
        alertChannel = null
    }

    private fun onNextAlertMessage(message: Message) {
        if (message.text.isBlank()) return

        launch {
            eventChannel.send(Event.ShowAlertMessage(message))
        }
    }

    sealed class Event {

        data class ShowAlertMessage(
            val message: Message
        ) : Event()
    }

    sealed class Router : RouterEvent {
        object SplashScreen : Router()
    }
}
