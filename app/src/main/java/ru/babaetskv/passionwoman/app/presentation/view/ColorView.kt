package ru.babaetskv.passionwoman.app.presentation.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.SweepGradient
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.animation.AnimationUtils
import androidx.annotation.ColorInt
import ru.babaetskv.passionwoman.app.R
import ru.babaetskv.passionwoman.app.utils.dip

class ColorView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var paint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
    }
    private var paintBorder = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.STROKE
    }
    private var borderWidth: Float
    private var canvasSize: Int? = null
    private var isMulticolor = false

    var borderVisible = false
        set(value) {
            field = value
            requestLayout()
            invalidate()
        }

    init {
        val attributes = context.obtainStyledAttributes(attrs, R.styleable.ColorView)
        borderWidth = attributes.getDimension(R.styleable.ColorView_cv_border_width, dip(DEFAULT_BORDER_WIDTH).toFloat())
        paint.color = attributes.getColor(R.styleable.ColorView_cv_color, Color.GRAY)
        paintBorder.color = attributes.getColor(R.styleable.ColorView_cv_border_color, Color.GRAY)
        attributes.recycle()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        canvasSize = if (h < w) h else w
    }

    override fun onDraw(canvas: Canvas) {
        val circleCenter = canvasSize?.minus(2 * borderWidth)?.div(2)
        val borderCenter = circleCenter?.plus(borderWidth) ?: 0F
        if (borderVisible) {
            val halfBorder = borderWidth / 2
            paintBorder.strokeWidth = borderWidth
            canvas.drawCircle(borderCenter, borderCenter, borderCenter - halfBorder, paintBorder)
        }
        if (isMulticolor) {
            paint.shader = SweepGradient(borderCenter, borderCenter, multicolorList, null)
            canvas.drawCircle(borderCenter, borderCenter, circleCenter ?: 0F, paint)
        } else canvas.drawCircle(borderCenter, borderCenter, circleCenter ?: 0F, paint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when(event.action) {
            MotionEvent.ACTION_DOWN -> {
                val scale = AnimationUtils.loadAnimation(context, R.anim.scale_shrink)
                startAnimation(scale)
            }
            MotionEvent.ACTION_UP -> {
                val scale = AnimationUtils.loadAnimation(context, R.anim.scale_enlarge)
                startAnimation(scale)
                performClick()
            }
        }
        return true
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = measureSize(widthMeasureSpec)
        val height = measureSize(heightMeasureSpec)
        setMeasuredDimension(width, height)
    }

    private fun measureSize(measureSpec: Int): Int {
        val specMode = MeasureSpec.getMode(measureSpec)
        val specSize = MeasureSpec.getSize(measureSpec)
        return when (specMode) {
            MeasureSpec.EXACTLY -> specSize
            MeasureSpec.AT_MOST -> specSize
            else -> canvasSize ?: 0
        }
    }

    fun setColor(@ColorInt color: Int) {
        paint.color = color
        isMulticolor = false
        requestLayout()
        invalidate()
    }

    fun setMulticolor() {
        isMulticolor = true
        requestLayout()
        invalidate()
    }

    companion object {
        const val DEFAULT_BORDER_WIDTH = 2

        val multicolorList = listOf(
            Color.parseColor("#33004c"),
            Color.parseColor("#4600d2"),
            Color.parseColor("#0000ff"),
            Color.parseColor("#0099ff"),
            Color.parseColor("#00eeff"),
            Color.parseColor("#00FF7F"),
            Color.parseColor("#48FF00"),
            Color.parseColor("#B6FF00"),
            Color.parseColor("#FFD700"),
            Color.parseColor("#ff9500"),
            Color.parseColor("#FF6200"),
            Color.parseColor("#FF0000"),
            Color.parseColor("#33004c")
        ).toIntArray()
    }
}
