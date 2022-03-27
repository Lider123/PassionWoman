package ru.babaetskv.passionwoman.app.presentation.view.highlight

import android.content.Context
import android.view.Window
import androidx.annotation.ColorInt
import ru.babaetskv.passionwoman.app.presentation.view.highlight.shape.Shape
import ru.babaetskv.passionwoman.app.presentation.view.highlight.target.Target
import ru.babaetskv.passionwoman.app.utils.dialog.DIALOG_ACTIONS_ORIENTATION_VERTICAL
import ru.babaetskv.passionwoman.app.utils.dialog.DialogAction
import ru.babaetskv.passionwoman.app.utils.dialog.showAlertDialog

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

    fun prepare(
        target: Target,
        animateShow: Boolean,
        window: Window,
        doOnShow: () -> Unit = {}
    ) {
        this.window = window
        target.calculateBorders {
            view.setFrameBorders(it)
            if (showOnReady) {
                if (animateShow) show(doOnShow) else showImmediately(doOnShow)
            }
        }
    }

    fun prepare(stage: Stage, window: Window) {
        prepare(stage.target, stage.animateShow, window) {
            view.context.showAlertDialog(
                message = stage.text,
                actionsOrientation = DIALOG_ACTIONS_ORIENTATION_VERTICAL,
                actions = listOf(
                    DialogAction(
                        text = stage.actionText,
                        isAccent = true,
                        callback = {
                            it.dismiss()
                            view.detachFromWindow(stage.animateHide)
                        }
                    )
                )
            ).also {
                it.setOnCancelListener {
                    if (stage.animateHide) hide() else hideImmediately()
                }
            }
        }
    }

    fun show(doOnShow: () -> Unit) {
        if (!isPrepared) throw IllegalStateException("Highlight is not prepared!")

        view.attachToWindow(window!!, true, doOnShow)
    }

    fun showImmediately(doOnShow: () -> Unit) {
        if (!isPrepared) throw IllegalStateException("Highlight is not prepared!")

        view.attachToWindow(window!!, false, doOnShow)
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

    data class Stage(
        val target: Target,
        val text: String,
        val actionText: String,
        val animateShow: Boolean = true,
        val animateHide: Boolean = true
    )
}
