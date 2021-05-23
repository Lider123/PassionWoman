package ru.babaetskv.passionwoman.app.auth

import android.content.Intent
import androidx.fragment.app.Fragment
import ru.babaetskv.passionwoman.app.R
import ru.babaetskv.passionwoman.domain.model.AuthResult

class AuthHandlerImpl @JvmOverloads internal constructor(
    private val fragment: Fragment,
    private val callback: AuthHandler.AuthCallback? = null
) : AuthHandler {
    private var smsListener: AuthHandler.OnSendSmsListener? = null

    override fun loginWithPhone(phone: String, smsListener: AuthHandler.OnSendSmsListener?) {
        if (phone.isEmpty()) {
            callback?.onLoginError(
                AuthException(fragment.getString(R.string.error_phone_number_is_null))
            )
            return
        }

        this.smsListener = smsListener
        smsListener?.onSendSms()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?): Boolean {
        if (requestCode != AuthHandler.REQUEST_PHONE_SIGN_IN) return false

        val smsCode = data?.getStringExtra(AuthHandler.EXTRA_SMS_CODE) ?: return false

        smsListener?.onSmsReceived(smsCode)
        callback?.onLoginSuccess(AuthResult("token", ""))
        return true
    }
}
