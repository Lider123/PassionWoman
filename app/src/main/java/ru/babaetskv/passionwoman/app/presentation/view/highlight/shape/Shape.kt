package ru.babaetskv.passionwoman.app.presentation.view.highlight.shape

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect

interface Shape {

    fun draw(canvas: Canvas, borders: Rect, paint: Paint)
}
