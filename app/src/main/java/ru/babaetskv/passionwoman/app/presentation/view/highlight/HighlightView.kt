package ru.babaetskv.passionwoman.app.presentation.view.highlight

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.graphics.Bitmap.createBitmap
import android.util.AttributeSet
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
    private val outlinePaint = Paint().apply {
        color = Color.GRAY
    }
    private var outlineBordersMultiplier: Float = 1f
    private var frameShape: Shape = CircleShape()
    private var frameBorders: Rect? = null
    private var frameMargin: Int = 0
    private val maxOutlineBordersMultiplier: Float
        get() {
            val viewDiagonal = sqrt(measuredWidth.toFloat().pow(2) + measuredHeight.toFloat().pow(2))
            val baseOutlineBorders = calculateOutlineBorders(1f)
            return 2 * viewDiagonal / max(baseOutlineBorders.width(), baseOutlineBorders.height())
        }
    private val frameBordersWithMargin: Rect?
        get() = frameBorders?.let {
            Rect(
                it.left - frameMargin,
                it.top - frameMargin,
                it.right + frameMargin,
                it.bottom + frameMargin
            )
        }

    override fun dispatchDraw(canvas: Canvas) {
        if (measuredWidth <= 0 && measuredHeight <= 0) return

        val overlay = createBitmap(measuredWidth, measuredHeight, Bitmap.Config.ARGB_8888)
        Canvas(overlay).run {
            frameShape.draw(this, calculateOutlineBorders(outlineBordersMultiplier), outlinePaint)
            alpha = 0.8f
            frameBordersWithMargin?.let {
                frameShape.draw(this, it, eraserPaint)
            }
        }
        canvas.drawBitmap(overlay, 0f, 0f, basicPaint)
        super.dispatchDraw(canvas)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        detachFromWindow()
        return true
    }

    private fun calculateOutlineBorders(sizeMultiplier: Float): Rect {
        val basedOn = frameBorders ?: Rect(0, 0, measuredWidth, measuredHeight)
        val basedWidth = basedOn.right - basedOn.left
        val basedHeight = basedOn.bottom - basedOn.top
        val width: Int = (if (basedWidth > basedHeight) basedWidth / basedHeight else 1).times(sizeMultiplier).toInt()
        val height: Int = (if (basedHeight > basedWidth) basedHeight / basedWidth else 1).times(sizeMultiplier).toInt()
        val posX: Int = basedOn.left + (basedWidth - width) / 2
        val posY: Int = basedOn.top + (basedHeight - height) / 2
        return Rect(posX, posY, posX + width, posY + height)
    }

    fun setFrameShape(shape: Shape) {
        frameShape = shape
        invalidate()
    }

    fun setFrameBorders(borders: Rect?) {
        frameBorders = borders
        invalidate()
    }

    fun setFrameMargin(margin: Int) {
        frameMargin = margin
        invalidate()
    }

    fun setOutlineColor(@ColorInt color: Int) {
        outlinePaint.color = color
        invalidate()
    }

    fun attachToWindow(window: Window, animate: Boolean = true) {
        with (window.decorView as ViewGroup) {
            addView(this@HighlightView)
        }
        if (animate) {
            post {
                ValueAnimator.ofFloat(1f, maxOutlineBordersMultiplier).apply {
                    duration = 1000L
                    interpolator = AccelerateInterpolator()
                    addUpdateListener {
                        outlineBordersMultiplier = it.animatedValue as Float
                        invalidate()
                    }
                }.start()
            }
        } else {
            outlineBordersMultiplier = maxOutlineBordersMultiplier
        }
    }

    fun detachFromWindow(animate: Boolean = true) {
        if (!animate) {
            (parent as? ViewGroup)?.removeView(this@HighlightView)
            outlineBordersMultiplier = 1f
            return
        }

        post {
            ValueAnimator.ofFloat(maxOutlineBordersMultiplier, 1f).apply {
                duration = 1000L
                interpolator = DecelerateInterpolator()
                addUpdateListener {
                    outlineBordersMultiplier = it.animatedValue as Float
                    invalidate()
                    if (outlineBordersMultiplier == 1f) {
                        (parent as? ViewGroup)?.removeView(this@HighlightView)
                    }
                }
            }.start()
        }
    }
}
