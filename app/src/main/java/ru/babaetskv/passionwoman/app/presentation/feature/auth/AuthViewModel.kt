package ru.babaetskv.passionwoman.app.presentation.feature.auth

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import ru.babaetskv.passionwoman.app.Screens
import ru.babaetskv.passionwoman.app.auth.AuthException
import ru.babaetskv.passionwoman.app.auth.AuthHandler
import ru.babaetskv.passionwoman.app.navigation.AppRouter
import ru.babaetskv.passionwoman.app.presentation.base.BaseViewModel
import ru.babaetskv.passionwoman.app.utils.notifier.Notifier
import ru.babaetskv.passionwoman.domain.interactor.AuthorizeAsGuestUseCase
import ru.babaetskv.passionwoman.domain.interactor.AuthorizeUseCase
import ru.babaetskv.passionwoman.domain.model.AuthResult
import ru.babaetskv.passionwoman.domain.preferences.AuthPreferences
import ru.babaetskv.passionwoman.domain.utils.execute

class AuthViewModel(
    private val authorizeUseCase: AuthorizeUseCase,
    private val authorizeAsGuestUseCase: AuthorizeAsGuestUseCase,
    private val authPreferences: AuthPreferences,
    notifier: Notifier,
    router: AppRouter
) : BaseViewModel(notifier, router), AuthHandler.AuthCallback, AuthHandler.OnSendSmsListener {
    private val eventChannel = Channel<Event>(Channel.RENDEZVOUS)

    val lastPhoneLiveData = MutableLiveData<String>()
    val smsCodeLiveData = MutableLiveData<String>()
    val modeLiveData = MutableLiveData(Mode.LOGIN)
    val eventBus: Flow<Event> = eventChannel.consumeAsFlow()

    override fun onBackPressed() {
        when (modeLiveData.value!!) {
            Mode.LOGIN -> super.onBackPressed()
            Mode.SMS_CONFIRM -> modeLiveData.postValue(Mode.LOGIN)
        }
    }

    override fun onLoginSuccess(authResult: AuthResult) {
        super.onLoginSuccess(authResult)
        loadingLiveData.postValue(false)
        launchWithLoading {
            val profile = authorizeUseCase.execute(authResult.token)
            authPreferences.profileIsFilled = profile.isFilled
            val screen = if (profile.isFilled) {
                Screens.navigation()
            } else {
                Screens.signUp(profile)
            }
            router.newRootScreen(screen)
        }
    }

    override fun onLoginCancel() {
        super.onLoginCancel()
        loadingLiveData.postValue(false)
    }

    override fun onLoginError(error: AuthException) {
        super.onLoginError(error)
        loadingLiveData.postValue(false)
        notifier.newRequest(this, error.message)
            .sendError()
    }

    override fun onSendSms() {
        loadingLiveData.postValue(false)
        modeLiveData.postValue(Mode.SMS_CONFIRM)
    }

    override fun onSmsReceived(code: String) {
        smsCodeLiveData.postValue(code)
    }

    fun onLoginPressed(phone: String, uiPhone: String) {
        lastPhoneLiveData.postValue(uiPhone)
        loadingLiveData.postValue(true)
        launch {
            eventChannel.send(Event.LoginWithPhone(phone))
        }
    }

    fun onGuestPressed() {
        launchWithLoading {
            authorizeAsGuestUseCase.execute()
            router.newRootScreen(Screens.navigation())
        }
    }

    enum class Mode {
        LOGIN, SMS_CONFIRM
    }

    sealed class Event {

        data class LoginWithPhone(
            val phone: String
        ) : Event()
    }
}
