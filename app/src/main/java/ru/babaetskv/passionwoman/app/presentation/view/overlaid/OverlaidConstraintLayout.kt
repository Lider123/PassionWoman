package ru.babaetskv.passionwoman.app.presentation.view.overlaid

import android.content.Context
import android.graphics.Canvas
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import ru.babaetskv.passionwoman.app.R

class OverlaidConstraintLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    private var colorMatrixProvider: ColorMatrixProvider = DefaultColorMatrixProvider()
        set(value) {
            field = value
            paint.colorFilter = ColorMatrixColorFilter(value.colorMatrix)
            requestLayout()
        }
    private val paint = Paint()

    var disabled: Boolean = false
        set(value) {
            field = value
            requestLayout()
        }

    init {
        context.theme.obtainStyledAttributes(attrs, R.styleable.OverlaidConstraintLayout, 0, 0).apply {
            try {
                val attrOverlay = getInt(R.styleable.OverlaidConstraintLayout_ocl_overlay, OVERLAY_NO)
                colorMatrixProvider = resolveColorMatrixProvider(attrOverlay)
            } finally {
                recycle()
            }
        }
    }

    override fun dispatchDraw(canvas: Canvas) {
        if (disabled) canvas.saveLayer(null, paint)
        super.dispatchDraw(canvas)
        if (disabled) canvas.restore()
    }

    override fun draw(canvas: Canvas) {
        if (disabled) canvas.saveLayer(null, paint)
        super.draw(canvas)
        if (disabled) canvas.restore()
    }

    private fun resolveColorMatrixProvider(overlay: Int): ColorMatrixProvider =
        when (overlay) {
            OVERLAY_NO -> DefaultColorMatrixProvider()
            OVERLAY_GREY -> GreyColorMatrixProvider()
            OVERLAY_ALPHA -> AlphaColorMatrixProvider()
            else -> throw IllegalArgumentException("Unknown value for attribute 'overlay': $overlay")
        }

    fun setOverlay(overlay: Int) {
        colorMatrixProvider = resolveColorMatrixProvider(overlay)
    }

    companion object {
        private const val OVERLAY_NO = 0
        private const val OVERLAY_GREY = 1
        private const val OVERLAY_ALPHA = 2
    }
}
