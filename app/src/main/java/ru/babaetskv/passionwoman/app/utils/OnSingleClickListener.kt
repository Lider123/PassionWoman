package ru.babaetskv.passionwoman.app.utils

import android.os.SystemClock
import android.view.View

abstract class OnSingleClickListener : View.OnClickListener {
    private var lastClickTime = 0L

    abstract fun onSingleClick(v: View)

    override fun onClick(v: View) {
        val currentClickTime = SystemClock.uptimeMillis()
        val elapsedTime = currentClickTime - lastClickTime
        lastClickTime = currentClickTime
        if (elapsedTime >= OFFSET_MILLIS) onSingleClick(v)
    }

    companion object {
        private const val OFFSET_MILLIS = 500L
    }
}
