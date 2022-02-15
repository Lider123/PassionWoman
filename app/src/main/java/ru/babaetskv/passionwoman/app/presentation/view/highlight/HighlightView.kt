package ru.babaetskv.passionwoman.app.presentation.view.highlight

import android.app.Activity
import android.content.Context
import android.graphics.*
import android.graphics.Bitmap.createBitmap
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewGroup
import android.view.Window
import android.widget.RelativeLayout
import ru.babaetskv.passionwoman.app.R
import ru.babaetskv.passionwoman.app.presentation.view.highlight.shape.CircleShape
import ru.babaetskv.passionwoman.app.presentation.view.highlight.shape.Shape
import ru.babaetskv.passionwoman.app.presentation.view.highlight.target.Target
import ru.babaetskv.passionwoman.app.utils.color

class HighlightView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {
    private var eraserPaint: Paint = Paint().apply {
        alpha = 0
        xfermode = PorterDuffXfermode(PorterDuff.Mode.MULTIPLY)
        isAntiAlias = true
    }
    private val basicPaint = Paint()
    private val contentBackgroundColor = color(R.color.secondary)
    private var frameBorders: Rect? = null

    var showOnReady: Boolean = false
    var frameMargin: Int = 0
    private val shape: Shape = CircleShape()

    override fun dispatchDraw(canvas: Canvas) {
        if (measuredWidth <= 0 && measuredHeight <= 0) return

        val overlay = createBitmap(measuredWidth, measuredHeight, Bitmap.Config.ARGB_8888)
        Canvas(overlay).run {
            drawColor(contentBackgroundColor)
            alpha = 0.8f
            frameBorders?.let {
                val frameBordersWithMargin = Rect(
                    it.left - frameMargin,
                    it.top - frameMargin,
                    it.right + frameMargin,
                    it.bottom + frameMargin
                )
                shape.draw(this, frameBordersWithMargin, eraserPaint)
            }
        }
        canvas.drawBitmap(overlay, 0f, 0f, basicPaint)
        super.dispatchDraw(canvas)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        hide()
        return true
    }

    private fun attachToWindow(window: Window) {
        with (window.decorView as ViewGroup) {
            addView(this@HighlightView)
        }
    }

    private fun detachFromWindow() {
        (parent as? ViewGroup)?.run {
            removeView(this@HighlightView)
        }
    }

    fun prepare(target: Target, activity: Activity) {
        target.calculateBorders {
            frameBorders = it
            if (showOnReady) attachToWindow(activity.window)
        }
    }

    fun hide() {
        detachFromWindow()
    }
}
