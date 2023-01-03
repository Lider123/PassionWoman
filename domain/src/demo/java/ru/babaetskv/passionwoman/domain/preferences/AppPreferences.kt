package ru.babaetskv.passionwoman.domain.preferences

interface AppPreferences {
    var onboardingShowed: Boolean
    var databaseInitialized: Boolean
    var databaseFilled: Boolean
    var demoOnStartShowed: Boolean
}