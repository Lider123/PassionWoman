package ru.babaetskv.passionwoman.app.presentation.view.highlight

import android.content.Context
import android.view.Window
import androidx.annotation.ColorInt
import ru.babaetskv.passionwoman.app.presentation.view.highlight.shape.Shape
import ru.babaetskv.passionwoman.app.presentation.view.highlight.target.Target

class Highlight private constructor(context: Context) {
    private val view = HighlightView(context)
    private var window: Window? = null

    var showOnReady: Boolean = true
    val isPrepared: Boolean
        get() = window != null

    private fun setShape(shape: Shape) {
        view.setFrameShape(shape)
    }

    private fun setMargin(margin: Int) {
        view.setFrameMargin(margin)
    }

    private fun setColor(@ColorInt color: Int) {
        view.setOutlineColor(color)
    }

    fun prepare(target: Target, window: Window) {
        this.window = window
        target.calculateBorders {
            view.setFrameBorders(it)
            if (showOnReady) show()
        }
    }

    fun show() {
        if (!isPrepared) throw IllegalStateException("Highlight is not prepared!")

        view.attachToWindow(window!!)
    }

    fun showImmediately() {
        if (!isPrepared) throw IllegalStateException("Highlight is not prepared!")

        view.attachToWindow(window!!, animate = false)
    }

    fun hide() {
        view.detachFromWindow()
    }

    fun hideImmediately() {
        view.detachFromWindow(animate = false)
    }

    class Builder(context: Context) {
        private val highlight = Highlight(context)

        fun setShape(shape: Shape) = apply {
            highlight.setShape(shape)
        }

        fun setFrameMargin(margin: Int) = apply {
            highlight.setMargin(margin)
        }

        fun setOutlineColor(@ColorInt color: Int) = apply {
            highlight.setColor(color)
        }

        fun build(): Highlight = highlight
    }
}
