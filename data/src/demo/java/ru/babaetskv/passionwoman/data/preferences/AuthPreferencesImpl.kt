package ru.babaetskv.passionwoman.data.preferences

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.flow.MutableStateFlow
import ru.babaetskv.passionwoman.domain.preferences.AuthPreferences

class AuthPreferencesImpl : AuthPreferences {
    private val userIdLiveData: MutableLiveData<Long>

    override var authType: AuthPreferences.AuthType = AuthPreferences.AuthType.NONE
        set(value) {
            field = value
            authTypeFlow.value = value
        }
    override var authToken: String = ""
    override var profileIsFilled: Boolean = false
    override val authTypeFlow = MutableStateFlow(authType)
    override var userId: Long = -1
        set(value) {
            field = value
            userIdLiveData.postValue(value)
        }

    init {
        userIdLiveData = MutableLiveData(userId)
    }

    override fun observeUserId(observer: (Long) -> Unit) {
        userIdLiveData.observeForever(observer)
    }

    override fun reset() {
        authType = AuthPreferences.AuthType.NONE
        authToken = ""
        userId = -1
        profileIsFilled = false
    }
}
