package ru.babaetskv.passionwoman.domain.preferences

interface AuthPreferences {
    var authType: AuthType
    var authToken: String
    var profileIsFilled: Boolean

    fun reset()

    enum class AuthType {
        AUTHORIZED, GUEST, NONE
    }
}
