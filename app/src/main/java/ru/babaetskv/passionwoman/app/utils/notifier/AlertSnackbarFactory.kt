package ru.babaetskv.passionwoman.app.utils.notifier

import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.core.view.updateLayoutParams
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import ru.babaetskv.passionwoman.app.R
import ru.babaetskv.passionwoman.app.presentation.view.AlertView

class AlertSnackbarFactory(
    private val context: Context,
    private val container: View,
    private val onVisibilityChanged: (visible: Boolean) -> Unit
) {

    fun create(topInset: Int, message: AlertMessage): Snackbar =
        Snackbar.make(container, "", Snackbar.LENGTH_LONG).apply {
            val alertView = LayoutInflater.from(context).inflate(
                R.layout.layout_alert_view,
                null,
                false
            )
            with (alertView as AlertView) {
                val iconRes: Int? = if (message.type == AlertMessage.Type.ERROR && message.iconRes == null) {
                    R.drawable.ic_error
                } else {
                    message.iconRes
                }
                setIcon(iconRes)
                this.message = message.text
                when (message.type) {
                    AlertMessage.Type.INFO -> R.color.alert_info
                    AlertMessage.Type.ERROR -> R.color.alert_error
                }.let {
                    setAlertColor(it)
                }
            }
            with (view as Snackbar.SnackbarLayout) {
                setBackgroundColor(Color.TRANSPARENT)
                this.updateLayoutParams<FrameLayout.LayoutParams> {
                    gravity = Gravity.TOP
                }
                val margin = resources.getDimensionPixelOffset(R.dimen.margin_small)
                setPadding(margin, margin + topInset, margin, margin)
                addView(alertView, 0)
            }
            addCallback(object : BaseTransientBottomBar.BaseCallback<Snackbar>() {

                override fun onShown(transientBottomBar: Snackbar?) {
                    super.onShown(transientBottomBar)
                    onVisibilityChanged.invoke(true)
                }

                override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                    super.onDismissed(transientBottomBar, event)
                    onVisibilityChanged.invoke(false)
                }
            })
        }
}
