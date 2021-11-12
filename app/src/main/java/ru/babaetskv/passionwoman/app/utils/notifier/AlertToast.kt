package ru.babaetskv.passionwoman.app.utils.notifier

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.Toast
import ru.babaetskv.passionwoman.app.R
import ru.babaetskv.passionwoman.app.presentation.view.AlertView
import ru.babaetskv.passionwoman.app.utils.color

// TODO: think about using snackbar instead of toast
class AlertToast private constructor(
    private val context: Context
): Toast(context) {

    init {
        setGravity(Gravity.FILL_HORIZONTAL or Gravity.TOP, 0, 0)
        duration = LENGTH_LONG
    }

    private fun setMessage(message: Message) {
        val alertView = LayoutInflater.from(context).inflate(
            R.layout.layout_alert_view,
            null,
            false
        ) as AlertView
        alertView.apply {
            val iconRes: Int? = if (message.type == Message.Type.ERROR && message.iconRes == null) {
                R.drawable.ic_error
            } else {
                message.iconRes
            }
            setIcon(iconRes)
            this.message = message.text
            when (message.type) {
                Message.Type.INFO -> R.color.alert_info
                Message.Type.ERROR -> R.color.alert_error
            }.let {
                setAlertColor(it)
            }
            setBackgroundColor(color(android.R.color.transparent))
        }
        val container = FrameLayout(context).apply {
            val layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT).apply {
                val margin = resources.getDimensionPixelOffset(R.dimen.margin_default)
                setMargins(margin, margin, margin, 0)
            }
            addView(alertView, layoutParams)
        }
        view = container
    }

    companion object {

        fun create(context: Context, message: Message) =
            AlertToast(context).apply {
                setMessage(message)
            }
    }
}
