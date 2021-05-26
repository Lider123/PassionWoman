package ru.babaetskv.passionwoman.data.preferences

import com.chibatching.kotpref.KotprefModel
import ru.babaetskv.passionwoman.domain.preferences.AppPreferences

class AppPreferencesImpl : KotprefModel(), AppPreferences {
    override var onboardingShowed: Boolean by booleanPref(false)
}
