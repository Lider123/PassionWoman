package ru.babaetskv.passionwoman.app.utils.notifier

import android.content.Context
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.launch

class Notifier(private val context: Context) {
    private val channel = BroadcastChannel<AlertMessage>(Channel.BUFFERED)

    fun subscribe(): ReceiveChannel<AlertMessage> = channel.openSubscription()

    fun newRequest(scope: CoroutineScope, text: String): Request =
        NotifierRequest(scope, text)

    fun newRequest(scope: CoroutineScope, @StringRes textRes: Int): Request =
        NotifierRequest(scope, textRes)

    abstract class Request {

        abstract fun withIcon(@DrawableRes iconRes: Int): Request

        abstract fun isImportant(value: Boolean): Request

        abstract fun sendAlert()

        abstract fun sendError()
    }

    private inner class NotifierRequest private constructor(
        private val scope: CoroutineScope
    ) : Request() {
        private var text: String = ""
        private var iconRes: Int? = null
        private var isImportant = true

        constructor(scope: CoroutineScope, text: String) : this(scope) {
            this.text = text
        }

        constructor(scope: CoroutineScope, @StringRes textRes: Int) : this(scope, context.getString(textRes))

        override fun withIcon(@DrawableRes iconRes: Int) = apply {
            this.iconRes = iconRes
        }

        override fun isImportant(value: Boolean) = apply {
            isImportant = value
        }

        override fun sendAlert() {
            val message = AlertMessage(
                text = text,
                iconRes = iconRes,
                isImportant = isImportant
            )
            scope.launch {
                channel.send(message)
            }
        }

        override fun sendError() {
            val message = AlertMessage(
                text = text,
                iconRes = iconRes,
                type = AlertMessage.Type.ERROR,
                isImportant = isImportant
            )
            scope.launch {
                channel.send(message)
            }
        }
    }
}
