package ru.babaetskv.passionwoman.app.presentation.view.highlightview

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import androidx.core.graphics.toRectF

sealed class Shape {

    abstract fun draw(canvas: Canvas, borders: Rect, paint: Paint)

    object Circle : Shape() {

        override fun draw(canvas: Canvas, borders: Rect, paint: Paint) {
            val width: Int = borders.right - borders.left
            val height: Int = borders.bottom - borders.top
            val cx: Int = borders.left + width / 2
            val cy: Int = borders.top + height / 2
            val radius: Float = if (width > height) 0.5f * width else 0.5f * height
            canvas.drawCircle(cx.toFloat(), cy.toFloat(), radius, paint)
        }
    }

    object Rectangle : Shape() {

        override fun draw(canvas: Canvas, borders: Rect, paint: Paint) {
            canvas.drawRect(borders, paint)
        }
    }

    class RoundedRectangle(
        private val radius: Float
    ) : Shape() {

        override fun draw(canvas: Canvas, borders: Rect, paint: Paint) {
            canvas.drawRoundRect(borders.toRectF(), radius, radius, paint)
        }
    }
}
