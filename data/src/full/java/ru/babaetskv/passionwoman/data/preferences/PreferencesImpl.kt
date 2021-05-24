package ru.babaetskv.passionwoman.data.preferences

import com.chibatching.kotpref.KotprefModel
import com.chibatching.kotpref.enumpref.enumValuePref

class PreferencesImpl : KotprefModel(), Preferences {
    override var onboardingShowed: Boolean by booleanPref(false)
    override var authToken: String by stringPref("")
    override var authType: Preferences.AuthType by enumValuePref(Preferences.AuthType.NONE)
}
