package ru.babaetskv.passionwoman.data.preferences

import ru.babaetskv.passionwoman.domain.preferences.AuthPreferences

class AuthPreferencesImpl : AuthPreferences {
    override var authType: AuthPreferences.AuthType = AuthPreferences.AuthType.NONE
    override var authToken: String = ""
    override var profileIsFilled: Boolean = false

    override fun reset() {
        authType = AuthPreferences.AuthType.NONE
        authToken = ""
        profileIsFilled = false
    }
}
