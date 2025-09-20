package ru.babaetskv.passionwoman.app.utils.externalaction

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.Intent.*
import android.net.Uri
import androidx.annotation.StringRes

class ExternalIntentHandler(
    private val context: Context
) : ExternalActionHandler {

    override fun handleText(text: String): Boolean =
        Intent(ACTION_SEND).apply {
            type = "text/plain"
            putExtra(EXTRA_TEXT, text)
            addFlags(FLAG_ACTIVITY_NEW_TASK)
        }.let {
            context.startActivitySafely(it)
        }

    override fun handleCall(phoneNumber: String): Boolean =
        Intent(ACTION_DIAL).apply {
            data = Uri.fromParts("tel", phoneNumber, null)
            addFlags(FLAG_ACTIVITY_NEW_TASK)
        }.let {
            context.startActivitySafely(it)
        }

    override fun handleCall(@StringRes phoneNumberRes: Int): Boolean =
        handleCall(context.getString(phoneNumberRes))

    override fun handleEmail(address: String): Boolean =
        Intent(ACTION_SENDTO).apply {
            data = Uri.parse("mailto:$address")
            addFlags(FLAG_ACTIVITY_NEW_TASK)
        }.let {
            context.startActivitySafely(it)
        }

    override fun handleEmail(@StringRes addressRes: Int): Boolean =
        handleEmail(context.getString(addressRes))

    override fun handleOuterLink(link: String): Boolean =
        Intent(ACTION_VIEW).apply {
            data = Uri.parse(link)
            addFlags(FLAG_ACTIVITY_NEW_TASK)
        }.let {
            context.startActivitySafely(it)
        }

    override fun handleOuterLink(@StringRes linkRes: Int): Boolean =
        handleOuterLink(context.getString(linkRes))

    private fun Context.startActivitySafely(intent: Intent): Boolean =
        try {
            startActivity(intent)
            true
        } catch (e: ActivityNotFoundException) {
            false
        }
}
