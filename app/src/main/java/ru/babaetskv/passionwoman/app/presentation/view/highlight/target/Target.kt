package ru.babaetskv.passionwoman.app.presentation.view.highlight.target

import android.graphics.Rect

interface Target {
    fun calculateBorders(onDone: (Rect) -> Unit)
}
