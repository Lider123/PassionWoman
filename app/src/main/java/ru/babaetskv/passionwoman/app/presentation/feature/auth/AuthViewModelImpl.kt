package ru.babaetskv.passionwoman.app.presentation.feature.auth

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.launch
import ru.babaetskv.passionwoman.app.analytics.event.OpenScreenEvent
import ru.babaetskv.passionwoman.app.auth.AuthException
import ru.babaetskv.passionwoman.app.navigation.Screens
import ru.babaetskv.passionwoman.app.presentation.base.BaseViewModel
import ru.babaetskv.passionwoman.app.presentation.base.ViewModelDependencies
import ru.babaetskv.passionwoman.domain.model.AuthResult
import ru.babaetskv.passionwoman.domain.preferences.AuthPreferences
import ru.babaetskv.passionwoman.domain.usecase.AuthorizeAsGuestUseCase
import ru.babaetskv.passionwoman.domain.usecase.AuthorizeUseCase
import ru.babaetskv.passionwoman.domain.usecase.base.UseCase.Companion.execute

class AuthViewModelImpl(
    private val args: AuthFragment.Args,
    private val authorizeUseCase: AuthorizeUseCase,
    private val authorizeAsGuestUseCase: AuthorizeAsGuestUseCase,
    private val authPreferences: AuthPreferences,
    dependencies: ViewModelDependencies
) : BaseViewModel(dependencies), AuthViewModel {
    override val lastPhoneLiveData = MutableLiveData<String>()
    override val smsCodeLiveData = MutableLiveData<String>()
    override val modeLiveData = MutableLiveData(AuthViewModel.AuthMode.LOGIN)
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
                    router.newRootScreen(Screens.navigation(null))
                } else super.onBackPressed()
            } else {
                router.run {
                    val screen = Screens.signUp(profile, args.onAppStart)
                    if (args.onAppStart) newRootScreen(screen) else replaceScreen(screen)
                }
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
            sendEvent(AuthViewModel.LoginWithPhoneEvent(phone))
        }
    }

    override fun onGuestPressed() {
        launchWithLoading {
            authorizeAsGuestUseCase.execute()
            router.newRootScreen(Screens.navigation(null))
        }
    }

    override fun onModeChanged(mode: AuthViewModel.AuthMode) {
        analyticsHandler.log(OpenScreenEvent(mode.screenName))
    }
}
