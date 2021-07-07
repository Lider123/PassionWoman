package ru.babaetskv.passionwoman.data.preferences

import ru.babaetskv.passionwoman.domain.preferences.AppPreferences

class AppPreferencesImpl : AppPreferences {
    override var onboardingShowed: Boolean = false
    override var favorites: MutableList<String> = mutableListOf()
}
