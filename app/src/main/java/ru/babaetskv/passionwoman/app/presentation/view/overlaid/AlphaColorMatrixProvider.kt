package ru.babaetskv.passionwoman.app.presentation.view.overlaid

import android.graphics.ColorMatrix

class AlphaColorMatrixProvider : ColorMatrixProvider {
    override val colorMatrix: ColorMatrix
        get() = ColorMatrix().apply {
            set(floatArrayOf(
                1f, 0f, 0f, 0f, 0f,
                0f, 1f, 0f, 0f, 0f,
                0f, 0f, 1f, 0f, 0f,
                0f, 0f, 0f, 0.4f, 0f
            ))
        }
}
