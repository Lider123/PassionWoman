package ru.babaetskv.passionwoman.app.utils.notifier

import android.content.Context
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.launch

class NotifierImpl(private val context: Context) : Notifier {
    private val channel = BroadcastChannel<AlertMessage>(Channel.BUFFERED)

    override fun subscribe(): ReceiveChannel<AlertMessage> = channel.openSubscription()

    override fun newRequest(scope: CoroutineScope, text: String): Notifier.Request =
        NotifierRequest(scope, text)

    override fun newRequest(scope: CoroutineScope, @StringRes textRes: Int): Notifier.Request =
        NotifierRequest(scope, textRes)

    private inner class NotifierRequest private constructor(
        private val scope: CoroutineScope
    ) : Notifier.Request {
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
