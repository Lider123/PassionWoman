package ru.babaetskv.passionwoman.data.preferences

import com.chibatching.kotpref.KotprefModel
import com.chibatching.kotpref.enumpref.enumValuePref
import ru.babaetskv.passionwoman.domain.preferences.AuthPreferences

class AuthPreferencesImpl : KotprefModel(), AuthPreferences {
    override var authToken: String by stringPref("")
    override var authType: AuthPreferences.AuthType by enumValuePref(AuthPreferences.AuthType.NONE)
    override var profileIsFilled: Boolean by booleanPref(false)

    override fun reset() {
        authToken = ""
        authType = AuthPreferences.AuthType.NONE
        profileIsFilled = false
    }
}
