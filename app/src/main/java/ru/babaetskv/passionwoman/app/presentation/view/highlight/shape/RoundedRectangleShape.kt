package ru.babaetskv.passionwoman.app.presentation.view.highlight.shape

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import androidx.core.graphics.toRectF

class RoundedRectangleShape(
    private val radius: Float
) : Shape {

    override fun draw(canvas: Canvas, borders: Rect, paint: Paint) {
        canvas.drawRoundRect(borders.toRectF(), radius, radius, paint)
    }
}
