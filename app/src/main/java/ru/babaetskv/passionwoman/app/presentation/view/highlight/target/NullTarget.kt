package ru.babaetskv.passionwoman.app.presentation.view.highlight.target

import android.graphics.Rect

class NullTarget : Target {

    override fun calculateBorders(onDone: (Rect?) -> Unit) {
        onDone.invoke(null)
    }
}
