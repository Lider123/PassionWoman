package ru.babaetskv.passionwoman.app.utils

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.Intent.*
import android.net.Uri
import androidx.annotation.StringRes

class ExternalIntentHandler(
    private val context: Context
) {

    private fun Context.startActivitySafely(intent: Intent): Boolean =
        try {
            startActivity(intent)
            true
        } catch (e: ActivityNotFoundException) {
            false
        }

    fun handleCall(phoneNumber: String): Boolean {
        val intent = Intent(ACTION_DIAL).apply {
            data = Uri.fromParts("tel", phoneNumber, null)
            addFlags(FLAG_ACTIVITY_NEW_TASK)
        }
        return context.startActivitySafely(intent)
    }

    fun handleCall(@StringRes phoneNumberRes: Int) = handleCall(context.getString(phoneNumberRes))

    fun handleEmail(address: String): Boolean {
        val intent = Intent(ACTION_SENDTO).apply {
            data = Uri.parse("mailto:$address")
            addFlags(FLAG_ACTIVITY_NEW_TASK)
        }
        return context.startActivitySafely(intent)
    }

    fun handleEmail(@StringRes addressRes: Int) = handleEmail(context.getString(addressRes))

    fun handleOuterLink(link: String): Boolean {
        val intent = Intent(ACTION_VIEW).apply {
            data = Uri.parse(link)
            addFlags(FLAG_ACTIVITY_NEW_TASK)
        }
        return context.startActivitySafely(intent)
    }

    fun handleOuterLink(@StringRes linkRes: Int) = handleOuterLink(context.getString(linkRes))
}
