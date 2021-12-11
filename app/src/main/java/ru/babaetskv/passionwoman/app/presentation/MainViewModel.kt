package ru.babaetskv.passionwoman.app.presentation

import android.content.Intent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import ru.babaetskv.passionwoman.app.presentation.base.BaseViewModel
import ru.babaetskv.passionwoman.app.presentation.base.RouterEvent
import ru.babaetskv.passionwoman.app.presentation.base.ViewModelDependencies
import ru.babaetskv.passionwoman.app.utils.deeplink.DeeplinkHandler
import ru.babaetskv.passionwoman.app.utils.deeplink.DeeplinkPayload
import ru.babaetskv.passionwoman.app.utils.notifier.AlertMessage

class MainViewModel(
    private val deeplinkHandler: DeeplinkHandler,
    dependencies: ViewModelDependencies
) : BaseViewModel<MainViewModel.Router>(dependencies) {
    private var alertChannel: ReceiveChannel<AlertMessage>? = null
    private val eventChannel = Channel<Event>(Channel.RENDEZVOUS)

    val eventBus: Flow<Event> = eventChannel.consumeAsFlow()

    override val logScreenOpening: Boolean = false

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



    fun handleIntent(intent: Intent, startApp: Boolean) {
        launch {
            val deeplinkPayload = deeplinkHandler.handle(intent.data)
            if (deeplinkPayload == null) {
                if (startApp) navigateTo(Router.SplashScreen(null))
            } else {
                if (startApp) {
                    navigateTo(Router.SplashScreen(deeplinkPayload))
                } else {
                    val screen: Router = when (deeplinkPayload) {
                        is DeeplinkPayload.Product -> Router.ProductScreen(deeplinkPayload.productId)
                    }
                    navigateTo(screen)
                }
            }
        }
    }

    sealed class Event {

        data class ShowAlertMessage(
            val message: AlertMessage
        ) : Event()
    }

    sealed class Router : RouterEvent {

        data class SplashScreen(
            val payload: DeeplinkPayload?
        ) : Router()

        data class ProductScreen(
            val productId: String
        ) : Router()
    }
}
