package ru.babaetskv.passionwoman.data.preferences

interface Preferences {
    var onboardingShowed: Boolean
    var authType: AuthType
    var authToken: String

    enum class AuthType {
        AUTHORIZED, GUEST, NONE
    }
}
