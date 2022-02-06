package ru.babaetskv.passionwoman.app.presentation.feature.auth

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import ru.babaetskv.passionwoman.app.analytics.event.OpenScreenEvent
import ru.babaetskv.passionwoman.app.auth.AuthException
import ru.babaetskv.passionwoman.app.auth.AuthHandler
import ru.babaetskv.passionwoman.app.presentation.base.BaseViewModel
import ru.babaetskv.passionwoman.app.presentation.event.RouterEvent
import ru.babaetskv.passionwoman.app.presentation.base.ViewModelDependencies
import ru.babaetskv.passionwoman.domain.interactor.AuthorizeAsGuestUseCase
import ru.babaetskv.passionwoman.domain.interactor.AuthorizeUseCase
import ru.babaetskv.passionwoman.domain.model.AuthResult
import ru.babaetskv.passionwoman.domain.model.Profile
import ru.babaetskv.passionwoman.domain.preferences.AuthPreferences
import ru.babaetskv.passionwoman.domain.utils.execute

class AuthViewModel(
    private val authorizeUseCase: AuthorizeUseCase,
    private val authorizeAsGuestUseCase: AuthorizeAsGuestUseCase,
    private val authPreferences: AuthPreferences,
    dependencies: ViewModelDependencies
) : BaseViewModel<AuthViewModel.Router>(dependencies),
    AuthHandler.AuthCallback,
    AuthHandler.OnSendSmsListener {
    private val eventChannel = Channel<Event>(Channel.RENDEZVOUS)

    val lastPhoneLiveData = MutableLiveData<String>()
    val smsCodeLiveData = MutableLiveData<String>()
    val modeLiveData = MutableLiveData(AuthMode.LOGIN)
    val eventBus: Flow<Event> = eventChannel.receiveAsFlow()

    override val logScreenOpening: Boolean = false

    override fun onBackPressed() {
        when (modeLiveData.value!!) {
            AuthMode.LOGIN -> super.onBackPressed()
            AuthMode.SMS_CONFIRM -> modeLiveData.postValue(AuthMode.LOGIN)
        }
    }

    override fun onLoginSuccess(authResult: AuthResult) {
        super.onLoginSuccess(authResult)
        loadingLiveData.postValue(false)
        launchWithLoading {
            val profile = authorizeUseCase.execute(authResult.token)
            authPreferences.profileIsFilled = profile.isFilled
            if (profile.isFilled) {
                navigateTo(Router.NavigationScreen)
            } else {
                navigateTo(Router.SignUpScreen(profile))
                smsCodeLiveData.postValue("")
                modeLiveData.postValue(AuthMode.LOGIN)
            }
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
        modeLiveData.postValue(AuthMode.SMS_CONFIRM)
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
            navigateTo(Router.NavigationScreen)
        }
    }

    fun onModeChanged(mode: AuthMode) {
        analyticsHandler.log(OpenScreenEvent(mode.screenName))
    }

    sealed class Event {

        data class LoginWithPhone(
            val phone: String
        ) : Event()
    }

    sealed class Router : RouterEvent {

        object NavigationScreen : Router()

        data class SignUpScreen(
            val profile: Profile
        ) : Router()
    }
}
