package ru.babaetskv.passionwoman.app.presentation.view.overlaid

import android.graphics.ColorMatrix

class DefaultColorMatrixProvider : ColorMatrixProvider {
    override val colorMatrix: ColorMatrix
        get() = ColorMatrix().apply {
            set(floatArrayOf(
                1f, 0f, 0f, 0f, 0f,
                0f, 1f, 0f, 0f, 0f,
                0f, 0f, 1f, 0f, 0f,
                0f, 0f, 0f, 1f, 0f
            ))
        }
}
