package ru.babaetskv.passionwoman.app.presentation.feature.auth

import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow
import ru.babaetskv.passionwoman.app.analytics.constants.ScreenKeys
import ru.babaetskv.passionwoman.app.auth.AuthHandler
import ru.babaetskv.passionwoman.app.presentation.base.IViewModel
import ru.babaetskv.passionwoman.app.presentation.event.RouterEvent
import ru.babaetskv.passionwoman.domain.model.Profile

interface AuthViewModel : IViewModel, AuthHandler.AuthCallback, AuthHandler.OnSendSmsListener {
    val lastPhoneLiveData: LiveData<String>
    val smsCodeLiveData: LiveData<String>
    val modeLiveData: LiveData<AuthMode>
    val eventBus: Flow<Event>

    fun onLoginPressed(phone: String, uiPhone: String)
    fun onGuestPressed()
    fun onModeChanged(mode: AuthMode)

    enum class AuthMode(val screenName: String) {
        LOGIN(ScreenKeys.LOGIN),
        SMS_CONFIRM(ScreenKeys.SMS_CONFIRM)
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
