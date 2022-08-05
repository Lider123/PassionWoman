package ru.babaetskv.passionwoman.app.presentation

import android.content.Intent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import ru.babaetskv.passionwoman.app.presentation.base.BaseViewModel
import ru.babaetskv.passionwoman.app.presentation.event.RouterEvent
import ru.babaetskv.passionwoman.app.presentation.base.ViewModelDependencies
import ru.babaetskv.passionwoman.app.utils.deeplink.DeeplinkHandler
import ru.babaetskv.passionwoman.app.utils.deeplink.DeeplinkPayload
import ru.babaetskv.passionwoman.app.utils.notifier.AlertMessage
import ru.babaetskv.passionwoman.domain.model.Profile
import ru.babaetskv.passionwoman.domain.preferences.AppPreferences
import ru.babaetskv.passionwoman.domain.preferences.AuthPreferences
import ru.babaetskv.passionwoman.domain.usecase.GetProfileUseCase

class MainViewModel(
    private val deeplinkHandler: DeeplinkHandler,
    private val authPreferences: AuthPreferences,
    private val appPreferences: AppPreferences,
    private val getProfileUseCase: GetProfileUseCase,
    dependencies: ViewModelDependencies
) : BaseViewModel<MainViewModel.Router>(dependencies) {
    private var alertChannel: ReceiveChannel<AlertMessage>? = null
    private val eventChannel = Channel<Event>(Channel.RENDEZVOUS)

    val eventBus: Flow<Event> = eventChannel.receiveAsFlow()
    var dataIsReady: Boolean = false
        private set

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

    private suspend fun navigateOnStart(payload: DeeplinkPayload?) {
        when {
            !appPreferences.onboardingShowed -> {
                navigateTo(Router.OnboardingScreen)
            }
            authPreferences.authType == AuthPreferences.AuthType.NONE -> {
                navigateTo(Router.AuthScreen)
            }
            authPreferences.authType == AuthPreferences.AuthType.AUTHORIZED
                    && !authPreferences.profileIsFilled -> launchWithLoading {
                val profile = getProfileUseCase.execute()
                navigateTo(Router.SignUpScreen(profile))
            }
            else -> navigateTo(Router.NavigationScreen(payload))
        }
    }

    private fun resolveRouter(payload: DeeplinkPayload?): Router? = when (payload) {
        is DeeplinkPayload.Product -> Router.ProductScreen(payload.productId)
        else -> null
    }

    fun handleIntent(intent: Intent, startApp: Boolean) {
        launch {
            val deeplinkPayload = deeplinkHandler.handle(intent.data)
            if (startApp) {
                navigateOnStart(deeplinkPayload)
            } else {
                resolveRouter(deeplinkPayload)?.let { navigateTo(it) }
            }
            dataIsReady = true
        }
    }

    sealed class Event {

        data class ShowAlertMessage(
            val message: AlertMessage
        ) : Event()
    }

    sealed class Router : RouterEvent {

        object OnboardingScreen : Router()

        object AuthScreen : Router()

        data class SignUpScreen(
            val profile: Profile
        ) : Router()

        data class NavigationScreen(
            val payload: DeeplinkPayload?
        ) : Router()

        data class ProductScreen(
            val productId: Int
        ) : Router()
    }
}
