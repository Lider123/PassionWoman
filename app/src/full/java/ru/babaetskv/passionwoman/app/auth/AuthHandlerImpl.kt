package ru.babaetskv.passionwoman.app.auth

import android.content.Intent
import androidx.fragment.app.Fragment
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import ru.babaetskv.passionwoman.app.R
import ru.babaetskv.passionwoman.domain.model.AuthResult
import java.util.concurrent.TimeUnit

class AuthHandlerImpl @JvmOverloads internal constructor(
    private val fragment: Fragment,
    private val callback: AuthHandler.AuthCallback? = null
) : AuthHandler {
    private var verificationId: String? = null
    private var smsListener: AuthHandler.OnSendSmsListener? = null

    override fun loginWithPhone(phone: String, smsListener: AuthHandler.OnSendSmsListener?) {
        if (phone.isEmpty()) {
            callback?.onLoginError(
                AuthException(fragment.getString(R.string.error_phone_number_is_null))
            )
            return
        }

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            phone,
            REQUEST_TIMEOUT.toLong(),
            TimeUnit.SECONDS,
            fragment.requireActivity(),
            object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
                    checkValidCredential(phoneAuthCredential)
                }

                override fun onVerificationFailed(e: FirebaseException) {
                    val message = when {
                        (e is FirebaseAuthInvalidCredentialsException)
                            && (e.errorCode == ERROR_INVALID_PHONE_NUMBER) -> fragment.getString(R.string.error_invalid_phone_number)
                        e is FirebaseNetworkException -> fragment.getString(R.string.error_network)
                        e.message?.contains("[ 7: ]") == true -> fragment.getString(R.string.error_network)
                        else -> fragment.getString(R.string.error_auth_failed)
                    }
                    callback?.onLoginError(AuthException(message))
                }

                override fun onCodeSent(
                    verificationId: String,
                    forceResendingToken: PhoneAuthProvider.ForceResendingToken
                ) {
                    super.onCodeSent(verificationId, forceResendingToken)
                    this@AuthHandlerImpl.smsListener = smsListener
                    this@AuthHandlerImpl.verificationId = verificationId
                    smsListener?.onSendSms()
                }
            }
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?): Boolean {
        if (requestCode != AuthHandler.REQUEST_PHONE_SIGN_IN) return false

        val smsCode = data?.getStringExtra(AuthHandler.EXTRA_SMS_CODE) ?: return false

        val credential = PhoneAuthProvider.getCredential(verificationId!!, smsCode)
        checkValidCredential(credential)
        return true
    }

    private fun checkValidCredential(credential: PhoneAuthCredential) {
        credential.smsCode?.let {
            smsListener?.onSmsReceived(it)
        }

        FirebaseAuth.getInstance().signInWithCredential(credential)
            .addOnCompleteListener(fragment.requireActivity()) { task ->
                if (task.isSuccessful && task.result != null) {
                    task.result?.user
                        ?.getIdToken(true)
                        ?.addOnCompleteListener { tokenTask ->
                            if (tokenTask.isSuccessful && tokenTask.result != null) {
                                callback?.onLoginSuccess(AuthResult(tokenTask.result!!.token!!, ""))
                            } else {
                                callback?.onLoginError(
                                    AuthException(fragment.getString(R.string.error_auth_failed))
                                )
                            }
                        }
                } else {
                    val message = when {
                        task.exception is FirebaseNetworkException -> fragment.getString(R.string.error_network)
                        task.exception?.message?.contains("[ 7: ]") == true -> fragment.getString(R.string.error_network)
                        else -> fragment.getString(R.string.error_wrong_sms_code)
                    }
                    callback?.onLoginError(AuthException(message))
                }
            }
    }

    companion object {
        private const val REQUEST_TIMEOUT = 120
        private const val ERROR_INVALID_PHONE_NUMBER = "ERROR_INVALID_PHONE_NUMBER"
    }
}
