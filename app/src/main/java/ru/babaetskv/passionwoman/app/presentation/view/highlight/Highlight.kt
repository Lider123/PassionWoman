package ru.babaetskv.passionwoman.app.presentation.view.highlight

import android.content.Context
import android.graphics.Color
import android.view.Window
import androidx.annotation.ColorInt
import ru.babaetskv.passionwoman.app.presentation.view.highlight.shape.CircleShape
import ru.babaetskv.passionwoman.app.presentation.view.highlight.shape.Shape
import ru.babaetskv.passionwoman.app.presentation.view.highlight.target.Target

class Highlight private constructor(
    context: Context,
    shape: Shape,
    frameMargin: Int,
    @ColorInt outlineColor: Int
) {
    private val view = HighlightView(context).apply {
        setFrameShape(shape)
        setFrameMargin(frameMargin)
        setOutlineColor(outlineColor)
    }

    var showOnReady: Boolean = false

    fun prepare(target: Target, window: Window) {
        target.calculateBorders {
            view.setFrameBorders(it)
            if (showOnReady) view.attachToWindow(window)
        }
    }

    class Builder(
        private val context: Context
    ) {
        private var shape: Shape = CircleShape()
        private var frameMargin: Int = 0
        @ColorInt
        private var outlineColor: Int = Color.GRAY

        fun setShape(shape: Shape) = apply {
            this.shape = shape
        }

        fun setFrameMargin(margin: Int) = apply {
            frameMargin = margin
        }

        fun setOutlineColor(@ColorInt color: Int) = apply {
            outlineColor = color
        }

        fun build(): Highlight = Highlight(context, shape, frameMargin, outlineColor)
    }
}
