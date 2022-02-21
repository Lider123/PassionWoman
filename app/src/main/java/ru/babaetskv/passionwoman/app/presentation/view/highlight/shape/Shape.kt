package ru.babaetskv.passionwoman.app.presentation.view.highlight.shape

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.view.View

interface Shape {
    fun draw(canvas: Canvas, borders: Rect, paint: Paint)
    fun modifyBordersToFit(borders: Rect, container: View): Rect
}
