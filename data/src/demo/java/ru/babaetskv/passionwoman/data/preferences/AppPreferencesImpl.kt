package ru.babaetskv.passionwoman.data.preferences

import com.chibatching.kotpref.KotprefModel
import ru.babaetskv.passionwoman.domain.preferences.AppPreferences

class AppPreferencesImpl : KotprefModel(), AppPreferences {
    override var onboardingShowed: Boolean by booleanPref()
    override var databaseFilled: Boolean by booleanPref()
    override var databaseInitialized: Boolean by booleanPref()
    override var demoOnStartShowed: Boolean by booleanPref()
    override var pushToken: String by stringPref()
}
