package ru.babaetskv.passionwoman.app.utils.notifier

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.ReceiveChannel

interface Notifier {
    fun subscribe(): ReceiveChannel<AlertMessage>
    fun newRequest(scope: CoroutineScope, text: String): Request
    fun newRequest(scope: CoroutineScope, @StringRes textRes: Int): Request

    interface Request {
        fun withIcon(@DrawableRes iconRes: Int): Request
        fun isImportant(value: Boolean): Request
        fun sendAlert()
        fun sendError()
    }
}
