package ru.babaetskv.passionwoman.app.presentation.feature.auth

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import ru.babaetskv.passionwoman.app.analytics.event.OpenScreenEvent
import ru.babaetskv.passionwoman.app.auth.AuthException
import ru.babaetskv.passionwoman.app.presentation.base.BaseViewModel
import ru.babaetskv.passionwoman.app.presentation.base.ViewModelDependencies
import ru.babaetskv.passionwoman.domain.model.AuthResult
import ru.babaetskv.passionwoman.domain.preferences.AuthPreferences
import ru.babaetskv.passionwoman.domain.usecase.AuthorizeAsGuestUseCase
import ru.babaetskv.passionwoman.domain.usecase.AuthorizeUseCase

class AuthViewModelImpl(
    private val args: AuthFragment.Args,
    private val authorizeUseCase: AuthorizeUseCase,
    private val authorizeAsGuestUseCase: AuthorizeAsGuestUseCase,
    private val authPreferences: AuthPreferences,
    dependencies: ViewModelDependencies
) : BaseViewModel<AuthViewModel.Router>(dependencies),
    AuthViewModel {
    private val eventChannel = Channel<AuthViewModel.Event>(Channel.RENDEZVOUS)

    override val lastPhoneLiveData = MutableLiveData<String>()
    override val smsCodeLiveData = MutableLiveData<String>()
    override val modeLiveData = MutableLiveData(AuthViewModel.AuthMode.LOGIN)
    override val eventBus: Flow<AuthViewModel.Event> = eventChannel.receiveAsFlow()
    override val logScreenOpening: Boolean = false

    override fun onBackPressed() {
        when (modeLiveData.value!!) {
            AuthViewModel.AuthMode.LOGIN -> super.onBackPressed()
            AuthViewModel.AuthMode.SMS_CONFIRM -> {
                modeLiveData.postValue(AuthViewModel.AuthMode.LOGIN)
            }
        }
    }

    override fun onLoginSuccess(authResult: AuthResult) {
        super.onLoginSuccess(authResult)
        loadingLiveData.postValue(false)
        launchWithLoading {
            val profile = authorizeUseCase.execute(authResult.token)
            authPreferences.profileIsFilled = profile.isFilled
            if (profile.isFilled) {
                if (args.onAppStart) {
                    navigateTo(AuthViewModel.Router.NavigationScreen)
                } else super.onBackPressed()
            } else {
                navigateTo(AuthViewModel.Router.SignUpScreen(profile, args.onAppStart))
                smsCodeLiveData.postValue("")
                modeLiveData.postValue(AuthViewModel.AuthMode.LOGIN)
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
        modeLiveData.postValue(AuthViewModel.AuthMode.SMS_CONFIRM)
    }

    override fun onSmsReceived(code: String) {
        smsCodeLiveData.postValue(code)
    }

    override fun onLoginPressed(phone: String, uiPhone: String) {
        lastPhoneLiveData.postValue(uiPhone)
        loadingLiveData.postValue(true)
        launch {
            eventChannel.send(AuthViewModel.Event.LoginWithPhone(phone))
        }
    }

    override fun onGuestPressed() {
        launchWithLoading {
            authorizeAsGuestUseCase.execute()
            navigateTo(AuthViewModel.Router.NavigationScreen)
        }
    }

    override fun onModeChanged(mode: AuthViewModel.AuthMode) {
        analyticsHandler.log(OpenScreenEvent(mode.screenName))
    }
}
