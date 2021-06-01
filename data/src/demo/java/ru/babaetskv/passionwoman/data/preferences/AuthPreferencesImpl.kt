package ru.babaetskv.passionwoman.data.preferences

import kotlinx.coroutines.flow.MutableStateFlow
import ru.babaetskv.passionwoman.domain.preferences.AuthPreferences

class AuthPreferencesImpl : AuthPreferences {
    override var authType: AuthPreferences.AuthType = AuthPreferences.AuthType.NONE
        set(value) {
            field = value
            authTypeFlow.value = value
        }
    override var authToken: String = ""
    override var profileIsFilled: Boolean = false
    override val authTypeFlow = MutableStateFlow(authType)

    override fun reset() {
        authType = AuthPreferences.AuthType.NONE
        authToken = ""
        profileIsFilled = false
    }
}
