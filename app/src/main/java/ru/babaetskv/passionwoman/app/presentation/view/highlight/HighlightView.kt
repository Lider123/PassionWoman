package ru.babaetskv.passionwoman.app.presentation.view.highlight

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.graphics.Bitmap.createBitmap
import android.util.AttributeSet
import android.util.Size
import android.view.MotionEvent
import android.view.ViewGroup
import android.view.Window
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.RelativeLayout
import androidx.annotation.ColorInt
import ru.babaetskv.passionwoman.app.presentation.view.highlight.shape.CircleShape
import ru.babaetskv.passionwoman.app.presentation.view.highlight.shape.Shape
import kotlin.math.max
import kotlin.math.pow
import kotlin.math.sqrt

internal class HighlightView @JvmOverloads constructor(
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
    private val outlinePaint = Paint()
    private var outlineMultiplier: Float = 1f
    private val maxMultiplier: Float
        get() {
            val viewDiagonal = sqrt(measuredWidth.toFloat().pow(2) + measuredHeight.toFloat().pow(2))
            val baseOutlineBordersSize = calculateOutlineBorders(1f).let {
                Size(
                    it.right - it.left,
                    it.bottom - it.top
                )
            }
            return 2 * viewDiagonal / max(baseOutlineBordersSize.width, baseOutlineBordersSize.height)
        }

    var frameBorders: Rect? = null
    var frameMargin: Int = 0
    var shape: Shape = CircleShape()

    override fun dispatchDraw(canvas: Canvas) {
        if (measuredWidth <= 0 && measuredHeight <= 0) return

        val overlay = createBitmap(measuredWidth, measuredHeight, Bitmap.Config.ARGB_8888)
        Canvas(overlay).run {
            shape.draw(this, calculateOutlineBorders(outlineMultiplier), outlinePaint)
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

    private fun detachFromWindow(animate: Boolean = true) {
        if (!animate) {
            (parent as? ViewGroup)?.removeView(this@HighlightView)
            return
        }

        post {
            ValueAnimator.ofFloat(maxMultiplier, 1f).apply {
                duration = 1000L
                interpolator = DecelerateInterpolator()
                addUpdateListener {
                    outlineMultiplier = it.animatedValue as Float
                    invalidate()
                    if (outlineMultiplier == 1f) {
                        (parent as? ViewGroup)?.removeView(this@HighlightView)
                    }
                }
            }.start()
        }
    }

    private fun hide() {
        detachFromWindow()
    }

    private fun calculateOutlineBorders(sizeMultiplier: Float): Rect {
        val basedOn = frameBorders ?: Rect(0, 0, measuredWidth, measuredHeight)
        val basedWidth = basedOn.right - basedOn.left
        val basedHeight = basedOn.bottom - basedOn.top
        val width: Int = (if (basedWidth > basedHeight) basedWidth / basedHeight else 1).times(sizeMultiplier).toInt()
        val height: Int = (if (basedHeight > basedWidth) basedHeight / basedWidth else 1).times(sizeMultiplier).toInt()
        val posX: Int = basedOn.left + (basedWidth - width) / 2
        val posY: Int = basedOn.top + (basedHeight - height) / 2
        val right = posX + width
        val bottom = posY + height
        return Rect(posX, posY, right, bottom)
    }

    fun setOutlineColor(@ColorInt color: Int) {
        outlinePaint.color = color
    }

    fun attachToWindow(window: Window, animate: Boolean = true) {
        with (window.decorView as ViewGroup) {
            addView(this@HighlightView)
        }
        if (animate) {
            post {
                val viewDiagonal = sqrt(measuredWidth.toFloat().pow(2) + measuredHeight.toFloat().pow(2))
                val baseOutlineBordersSize = calculateOutlineBorders(1f).let {
                    Size(
                        it.right - it.left,
                        it.bottom - it.top
                    )
                }
                val maxMultiplier = 2 * viewDiagonal / max(baseOutlineBordersSize.width, baseOutlineBordersSize.height)
                ValueAnimator.ofFloat(1f, maxMultiplier).apply {
                    duration = 1000L
                    interpolator = AccelerateInterpolator()
                    addUpdateListener {
                        outlineMultiplier = it.animatedValue as Float
                        invalidate()
                    }
                }.start()
            }
        }
    }
}
