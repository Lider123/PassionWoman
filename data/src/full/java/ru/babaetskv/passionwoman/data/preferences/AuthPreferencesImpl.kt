package ru.babaetskv.passionwoman.data.preferences

import com.chibatching.kotpref.KotprefModel
import com.chibatching.kotpref.enumpref.enumValuePref
import kotlinx.coroutines.flow.MutableStateFlow
import ru.babaetskv.passionwoman.domain.preferences.AuthPreferences

class AuthPreferencesImpl : KotprefModel(), AuthPreferences {
    private var _authType: AuthPreferences.AuthType by enumValuePref(AuthPreferences.AuthType.NONE)

    override var authToken: String by stringPref("")
    override var authType: AuthPreferences.AuthType
        get() = _authType
        set(value) {
            _authType = value
            authTypeFlow.value = value
        }
    override var profileIsFilled: Boolean by booleanPref(false)
    override val authTypeFlow = MutableStateFlow(authType)
    override val userId: String by stringPref("null")

    override fun reset() {
        authToken = ""
        userId = "null"
        authType = AuthPreferences.AuthType.NONE
        profileIsFilled = false
    }
}
