package ru.babaetskv.passionwoman.app.presentation.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import com.facebook.shimmer.ShimmerFrameLayout
import ru.babaetskv.passionwoman.app.R
import ru.babaetskv.passionwoman.app.utils.color

class LinearMockView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ShimmerFrameLayout(context, attrs, defStyleAttr) {
    private val itemsMargin: Int
    private val itemViewRes: Int
    private val backgroundRes: Int

    init {
        context.theme.obtainStyledAttributes(attrs, R.styleable.LinearMockView, 0, 0).apply {
            try {
                itemsMargin = getDimension(R.styleable.LinearMockView_lmv_margin, 0f).toInt()
                itemViewRes = getResourceId(R.styleable.LinearMockView_lmv_itemView, R.layout.layout_mock_default)
                backgroundRes = getResourceId(R.styleable.LinearMockView_android_background, R.color.background)
            } finally {
                recycle()
            }
        }
        val container = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(itemsMargin, itemsMargin, itemsMargin, itemsMargin)
            setBackgroundResource(backgroundRes)
        }
        addView(container, LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        ))
        for (i in 0 until ITEMS_COUNT_DEFAULT) {
            val marginParams = LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT
            ).apply {
                if (i != 0) setMargins(0, itemsMargin, 0, 0)
            }
            val childView: View = LayoutInflater.from(context).inflate(itemViewRes, null)
            container.addView(childView, marginParams)
        }
    }

    companion object {
        private const val ITEMS_COUNT_DEFAULT = 6
    }
}
