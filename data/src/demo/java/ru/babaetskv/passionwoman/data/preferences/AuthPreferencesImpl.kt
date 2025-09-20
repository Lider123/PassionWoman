package ru.babaetskv.passionwoman.data.preferences

import androidx.lifecycle.MutableLiveData
import com.chibatching.kotpref.KotprefModel
import com.chibatching.kotpref.enumpref.enumOrdinalPref
import kotlinx.coroutines.flow.MutableStateFlow
import ru.babaetskv.passionwoman.domain.preferences.AuthPreferences

class AuthPreferencesImpl : KotprefModel(), AuthPreferences {
    private val userIdLiveData: MutableLiveData<Long>
    private var userIdPref: Long by longPref()
    private var authTypePref: AuthPreferences.AuthType by enumOrdinalPref(AuthPreferences.AuthType.NONE)

    override var authType: AuthPreferences.AuthType
        get() = authTypePref
        set(value) {
            authTypePref = value
            authTypeFlow.value = value
        }
    override var authToken: String by stringPref()
    override var profileIsFilled: Boolean by booleanPref()
    override val authTypeFlow = MutableStateFlow(authType)
    override var userId: Long
        get() = userIdPref
        set(value) {
            userIdPref = value
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
