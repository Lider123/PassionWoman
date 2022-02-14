package ru.babaetskv.passionwoman.app.presentation.view.highlightview

import android.content.Context
import android.graphics.*
import android.graphics.Bitmap.createBitmap
import android.util.AttributeSet
import android.widget.RelativeLayout
import ru.babaetskv.passionwoman.app.R
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
    private val shape: Shape = Shape.RoundedRectangle(45f)

    override fun dispatchDraw(canvas: Canvas) {
        val item = Rect(
            (0.2 * measuredWidth).toInt(),
            (0.2 * measuredWidth).toInt(),
            (0.5 * measuredWidth).toInt(),
            (0.5 * measuredWidth).toInt()
        )
        val overlay = createBitmap(measuredWidth, measuredHeight, Bitmap.Config.ARGB_8888)
        Canvas(overlay).run {
            drawColor(contentBackgroundColor)
            alpha = 0.8f
            shape.draw(this, item, eraserPaint)
        }
        canvas.drawBitmap(overlay, 0f, 0f, basicPaint)
        super.dispatchDraw(canvas)
    }
}
