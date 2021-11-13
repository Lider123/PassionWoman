package ru.babaetskv.passionwoman.app.presentation

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import ru.babaetskv.passionwoman.app.presentation.base.BaseViewModel
import ru.babaetskv.passionwoman.app.presentation.base.RouterEvent
import ru.babaetskv.passionwoman.app.presentation.base.ViewModelDependencies
import ru.babaetskv.passionwoman.app.utils.notifier.AlertMessage

class MainViewModel(
    dependencies: ViewModelDependencies
) : BaseViewModel<MainViewModel.Router>(dependencies) {
    private var alertChannel: ReceiveChannel<AlertMessage>? = null
    private val eventChannel = Channel<Event>(Channel.RENDEZVOUS)

    val eventBus: Flow<Event> = eventChannel.consumeAsFlow()

    override val logScreenOpening: Boolean = false

    init {
        launch {
            navigateTo(Router.SplashScreen)
        }
    }

    override fun onStart(screenName: String) {
        super.onStart(screenName)
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

    private fun onNextAlertMessage(message: AlertMessage) {
        if (message.text.isBlank()) return

        launch {
            eventChannel.send(Event.ShowAlertMessage(message))
        }
    }

    sealed class Event {

        data class ShowAlertMessage(
            val message: AlertMessage
        ) : Event()
    }

    sealed class Router : RouterEvent {
        object SplashScreen : Router()
    }
}
