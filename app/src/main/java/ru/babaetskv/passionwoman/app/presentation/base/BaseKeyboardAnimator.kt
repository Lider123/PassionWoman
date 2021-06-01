package ru.babaetskv.passionwoman.app.presentation.base

import android.view.View
import android.view.Window
import android.view.WindowManager

abstract class BaseKeyboardAnimator(
    private val window: Window
) {
    protected abstract val insetsListener: View.OnApplyWindowInsetsListener

    init {
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
    }

    fun start() = window.decorView.setOnApplyWindowInsetsListener(insetsListener)

    fun stop() = window.decorView.setOnApplyWindowInsetsListener(null)
}
