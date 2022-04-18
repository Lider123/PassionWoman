package ru.babaetskv.passionwoman.app.presentation.view.overlaid

import android.graphics.ColorMatrix

class GreyColorMatrixProvider : ColorMatrixProvider {
    override val colorMatrix: ColorMatrix
        get() = ColorMatrix().apply {
            set(floatArrayOf(
                0.33f, 0.33f, 0.33f, 0f, 0f,
                0.33f, 0.33f, 0.33f, 0f, 0f,
                0.33f, 0.33f, 0.33f, 0f, 0f,
                0f, 0f, 0f, 1f, 0f
            ))
        }
}
