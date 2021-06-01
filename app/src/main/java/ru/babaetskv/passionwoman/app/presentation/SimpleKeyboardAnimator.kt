package ru.babaetskv.passionwoman.app.presentation

import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.transition.ChangeBounds
import androidx.transition.TransitionManager
import ru.babaetskv.passionwoman.app.presentation.base.BaseKeyboardAnimator

class SimpleKeyboardAnimator(window: Window) : BaseKeyboardAnimator(window) {
    private val sceneRoot: ViewGroup? by lazy(LazyThreadSafetyMode.NONE) {
        window.decorView.findViewById<View>(Window.ID_ANDROID_CONTENT)?.parent as? ViewGroup
    }

    override val insetsListener: View.OnApplyWindowInsetsListener
        get() = View.OnApplyWindowInsetsListener { view, insets ->
            sceneRoot?.let { TransitionManager.beginDelayedTransition(it, ChangeBounds()) }
            return@OnApplyWindowInsetsListener view.onApplyWindowInsets(insets)
        }
}
