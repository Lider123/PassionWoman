package ru.babaetskv.passionwoman.app.presentation

import android.content.Intent
import com.github.terrakok.cicerone.Screen
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import ru.babaetskv.passionwoman.app.navigation.Screens
import ru.babaetskv.passionwoman.app.presentation.base.BaseViewModel
import ru.babaetskv.passionwoman.app.presentation.base.ViewModelDependencies
import ru.babaetskv.passionwoman.app.utils.deeplink.DeeplinkHandler
import ru.babaetskv.passionwoman.app.utils.deeplink.DeeplinkPayload
import ru.babaetskv.passionwoman.app.utils.notifier.AlertMessage
import ru.babaetskv.passionwoman.domain.preferences.AppPreferences
import ru.babaetskv.passionwoman.domain.preferences.AuthPreferences
import ru.babaetskv.passionwoman.domain.usecase.GetProfileUseCase
import ru.babaetskv.passionwoman.domain.usecase.base.UseCase.Companion.execute

class MainViewModelImpl(
    private val deeplinkHandler: DeeplinkHandler,
    private val authPreferences: AuthPreferences,
    private val appPreferences: AppPreferences,
    private val getProfileUseCase: GetProfileUseCase,
    dependencies: ViewModelDependencies
) : BaseViewModel(dependencies), MainViewModel {
    private var alertChannel: ReceiveChannel<AlertMessage>? = null

    override var dataIsReady: Boolean = false
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

    override fun handleIntent(intent: Intent, startApp: Boolean) {
        // TODO: handle notification
        launch {
            val deeplinkPayload = deeplinkHandler.handle(intent.data)
            if (startApp) {
                navigateOnStart(deeplinkPayload)
            } else {
                resolveScreen(deeplinkPayload)
                    ?.let(router::navigateTo)
            }
            dataIsReady = true
        }
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
            sendEvent(MainViewModel.ShowAlertMessageEvent(message))
        }
    }

    private fun navigateOnStart(payload: DeeplinkPayload?) {
        when {
            !appPreferences.onboardingShowed -> router.newRootScreen(Screens.onboarding())
            authPreferences.authType == AuthPreferences.AuthType.NONE -> {
                router.newRootScreen(Screens.auth(true))
            }
            authPreferences.authType == AuthPreferences.AuthType.AUTHORIZED
                && !authPreferences.profileIsFilled -> launchWithLoading {
                val profile = getProfileUseCase.execute()
                router.newRootScreen(Screens.signUp(profile, true))
            }
            else -> router.newRootScreen(Screens.navigation(payload))
        }
    }

    private fun resolveScreen(payload: DeeplinkPayload?): Screen? = when (payload) {
        is DeeplinkPayload.Product -> Screens.productCard(payload.productId)
        else -> null
    }
}
