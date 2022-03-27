package ru.babaetskv.passionwoman.app.presentation.view.highlight.shape

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import androidx.core.graphics.toRectF
import android.view.View
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sqrt

class RoundedRectangleShape(
    private val radius: Float
) : Shape {

    override fun draw(canvas: Canvas, borders: Rect, paint: Paint) {
        canvas.drawRoundRect(borders.toRectF(), radius, radius, paint)
    }

    override fun modifyBordersToFit(borders: Rect, container: View): Rect {
        val radiusMargin = (1f - sqrt(2f).div(2)).times(radius).toInt()
        return Rect(
            max(borders.left, 0 - radiusMargin),
            max(borders.top, 0 - radiusMargin),
            min(borders.right, container.measuredWidth + radiusMargin),
            min(borders.bottom, container.measuredHeight + radiusMargin)
        )
    }
}
