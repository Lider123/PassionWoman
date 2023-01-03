package ru.babaetskv.passionwoman.app.presentation

import android.content.Intent
import com.github.terrakok.cicerone.Screen
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import ru.babaetskv.passionwoman.app.navigation.ScreenProvider
import ru.babaetskv.passionwoman.app.presentation.base.BaseViewModel
import ru.babaetskv.passionwoman.app.presentation.base.ViewModelDependencies
import ru.babaetskv.passionwoman.app.utils.deeplink.DeeplinkHandler
import ru.babaetskv.passionwoman.app.utils.deeplink.DeeplinkPayload
import ru.babaetskv.passionwoman.app.utils.notifier.AlertMessage
import ru.babaetskv.passionwoman.domain.preferences.AppPreferences
import ru.babaetskv.passionwoman.domain.preferences.AuthPreferences
import ru.babaetskv.passionwoman.domain.usecase.GetProfileUseCase
import ru.babaetskv.passionwoman.domain.usecase.base.UseCase.Companion.execute

abstract class BaseMainViewModelImpl(
    private val deeplinkHandler: DeeplinkHandler,
    private val authPrefs: AuthPreferences,
    protected val appPrefs: AppPreferences,
    private val getProfileUseCase: GetProfileUseCase,
    dependencies: ViewModelDependencies
) : BaseViewModel(dependencies), MainViewModel {
    private var alertChannel: ReceiveChannel<AlertMessage>? = null

    override var appIsReady: Boolean = false
        protected set
    override val logScreenOpening: Boolean = false

    override fun onStart(screenName: String) {
        super.onStart(screenName)
        subscribeOnAlerts()
        prepareApp()
    }

    override fun onStop() {
        unsubscribeFromAlerts()
        super.onStop()
    }

    override fun handleIntent(intent: Intent, startApp: Boolean) {
        launch {
            val deeplinkPayload = deeplinkHandler.handle(intent.data)
            if (startApp) {
                navigateOnStart(deeplinkPayload)
            } else {
                resolveScreen(deeplinkPayload)
                    ?.let(router::navigateTo)
            }
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

    protected open fun navigateOnStart(payload: DeeplinkPayload?) {
        when {
            !appPrefs.onboardingShowed -> router.newRootScreen(ScreenProvider.onboarding())
            authPrefs.authType == AuthPreferences.AuthType.NONE -> {
                router.newRootScreen(ScreenProvider.auth(true))
            }
            authPrefs.authType == AuthPreferences.AuthType.AUTHORIZED
                && !authPrefs.profileIsFilled -> launchWithLoading {
                val profile = getProfileUseCase.execute()
                router.newRootScreen(ScreenProvider.signUp(profile, true))
            }
            else -> router.newRootScreen(ScreenProvider.navigation(payload))
        }
    }

    private fun resolveScreen(payload: DeeplinkPayload?): Screen? = when (payload) {
        is DeeplinkPayload.Product -> ScreenProvider.productCard(payload.productId)
        else -> null
    }

    protected open fun prepareApp() {
        appIsReady = true
    }
}
