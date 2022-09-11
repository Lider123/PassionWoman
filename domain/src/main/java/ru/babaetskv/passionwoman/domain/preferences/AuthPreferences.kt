package ru.babaetskv.passionwoman.domain.preferences

import kotlinx.coroutines.flow.Flow

interface AuthPreferences {
    var authType: AuthType
    var authToken: String
    var userId: Int
    var profileIsFilled: Boolean
    val authTypeFlow: Flow<AuthType>

    fun observeUserId(observer: (Int) -> Unit)
    fun reset()

    enum class AuthType {
        NONE, AUTHORIZED, GUEST
    }
}
