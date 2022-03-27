package ru.babaetskv.passionwoman.app.presentation.view.highlight.shape

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import kotlin.math.max
import kotlin.math.min

class RectangleShape : Shape {

    override fun draw(canvas: Canvas, borders: Rect, paint: Paint) {
        canvas.drawRect(borders, paint)
    }

    override fun modifyBordersToFit(borders: Rect, container: View): Rect =
        Rect(
            max(borders.left, 0),
            max(borders.top, 0),
            min(borders.right, container.measuredWidth),
            min(borders.bottom, container.measuredHeight)
        )
}
