package ru.babaetskv.passionwoman.app.presentation.view.highlight.target

import android.graphics.Rect
import android.view.View
import android.view.ViewTreeObserver

class ViewTarget(
    private val view: View
) : Target {

    override fun calculateBorders(onDone: (Rect) -> Unit) {
        var listener = ViewTreeObserver.OnGlobalLayoutListener {}
        listener = ViewTreeObserver.OnGlobalLayoutListener {
            val location = IntArray(2)
            view.getLocationOnScreen(location)
            val borders = Rect(
                location[0],
                location[1],
                location[0] + view.measuredWidth,
                location[1] + view.measuredHeight
            )
            onDone.invoke(borders)
            view.viewTreeObserver.removeOnGlobalLayoutListener(listener)

        }
        view.viewTreeObserver.addOnGlobalLayoutListener(listener)
    }
}
