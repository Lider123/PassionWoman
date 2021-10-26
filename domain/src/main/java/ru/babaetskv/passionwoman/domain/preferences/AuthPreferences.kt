package ru.babaetskv.passionwoman.domain.preferences

import kotlinx.coroutines.flow.Flow

interface AuthPreferences {
    var authType: AuthType
    var authToken: String
    var userId: String
    var profileIsFilled: Boolean
    val authTypeFlow: Flow<AuthType>

    fun reset()

    enum class AuthType {
        NONE, AUTHORIZED, GUEST
    }
}
