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
    private val channel = BroadcastChannel<Message>(Channel.BUFFERED)

    fun subscribe(): ReceiveChannel<Message> = channel.openSubscription()

    fun newRequest(scope: CoroutineScope, text: String): Request =
        NotifierRequest(scope, text)

    fun newRequest(scope: CoroutineScope, @StringRes textRes: Int): Request =
        NotifierRequest(scope, textRes)

    abstract class Request {

        abstract fun withIcon(@DrawableRes iconRes: Int): Request

        abstract fun sendAlert()

        abstract fun sendError()
    }

    private inner class NotifierRequest private constructor(
        private val scope: CoroutineScope
    ) : Request() {
        private var text: String = ""
        private var iconRes: Int? = null

        constructor(scope: CoroutineScope, text: String) : this(scope) {
            this.text = text
        }

        constructor(scope: CoroutineScope, @StringRes textRes: Int) : this(scope, context.getString(textRes))

        override fun withIcon(@DrawableRes iconRes: Int) = apply {
            this.iconRes = iconRes
        }

        override fun sendAlert() {
            val message = Message(
                text = text,
                iconRes = iconRes
            )
            scope.launch {
                channel.send(message)
            }
        }

        override fun sendError() {
            val message = Message(
                text = text,
                iconRes = iconRes,
                type = Message.Type.ERROR
            )
            scope.launch {
                channel.send(message)
            }
        }
    }
}
