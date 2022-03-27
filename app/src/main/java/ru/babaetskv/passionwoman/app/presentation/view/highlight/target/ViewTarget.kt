package ru.babaetskv.passionwoman.app.presentation.view.highlight.target

import android.graphics.Rect
import android.view.View

class ViewTarget(
    private val view: View
) : Target {

    override fun calculateBorders(onDone: (Rect?) -> Unit) {
        view.post {
            val location = IntArray(2)
            view.getLocationOnScreen(location)
            val borders = Rect(
                location[0],
                location[1],
                location[0] + view.measuredWidth,
                location[1] + view.measuredHeight
            )
            onDone.invoke(borders)
        }
    }
}
