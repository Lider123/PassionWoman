package ru.babaetskv.passionwoman.app.auth

import android.content.Intent
import ru.babaetskv.passionwoman.domain.model.AuthResult

interface AuthHandler {
    fun loginWithPhone(phone: String, smsListener: OnSendSmsListener?)
    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?): Boolean

    interface AuthCallback {
        fun onLoginSuccess(authResult: AuthResult) = Unit
        fun onLoginCancel() = Unit
        fun onLoginError(error: AuthException) = Unit
    }

    interface OnSendSmsListener {
        fun onSendSms()
        fun onSmsReceived(code: String)
    }

    companion object {
        const val EXTRA_SMS_CODE = "SMS_CODE"
        const val REQUEST_PHONE_SIGN_IN = 100
        const val SMS_CODE_LENGTH = 6
    }
}