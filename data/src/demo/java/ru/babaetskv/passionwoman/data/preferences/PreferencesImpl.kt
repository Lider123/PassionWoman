package ru.babaetskv.passionwoman.data.preferences

class PreferencesImpl : Preferences {
    override var onboardingShowed: Boolean = false
    override var authType: Preferences.AuthType = Preferences.AuthType.NONE
    override var authToken: String = ""
}
