package ru.babaetskv.passionwoman.app.presentation.view.highlight.shape

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect

class RectangleShape : Shape {

    override fun draw(canvas: Canvas, borders: Rect, paint: Paint) {
        canvas.drawRect(borders, paint)
    }
}
