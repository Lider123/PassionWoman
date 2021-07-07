package ru.babaetskv.passionwoman.domain.preferences

interface AppPreferences {
    var onboardingShowed: Boolean
    var favorites: MutableList<String>
}