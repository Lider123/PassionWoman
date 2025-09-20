package ru.babaetskv.passionwoman.app.presentation.view

import android.content.Context
import android.graphics.PorterDuff
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.AppCompatImageButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.core.view.setPadding
import androidx.core.widget.TextViewCompat
import ru.babaetskv.passionwoman.app.R
import ru.babaetskv.passionwoman.app.databinding.ViewToolbarBinding
import ru.babaetskv.passionwoman.app.utils.color
import ru.babaetskv.passionwoman.app.utils.dimen
import ru.babaetskv.passionwoman.app.utils.inflateLayout
import ru.babaetskv.passionwoman.app.utils.setOnSingleClickListener

class ToolbarView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    private val binding: ViewToolbarBinding

    var title: String
        get() = binding.tvTitle.text.toString()
        set(value) {
            binding.tvTitle.text = value
        }

    init {
        inflateLayout(R.layout.view_toolbar, true)
        binding = ViewToolbarBinding.bind(this)
        context.theme.obtainStyledAttributes(attrs, R.styleable.ToolbarView, 0, 0).apply {
            try {
                title = getString(R.styleable.ToolbarView_tv_title) ?: context.getString(R.string.app_name)
                binding.tvTitle.textAlignment =
                    getInteger(R.styleable.ToolbarView_android_textAlignment, TEXT_ALIGNMENT_CENTER)
                getResourceId(R.styleable.ToolbarView_tv_titleTextAppearance, -1).let {
                    if (it == -1) null else it
                }?.let {
                    TextViewCompat.setTextAppearance(binding.tvTitle, it)
                }
            } finally {
                recycle()
            }
        }
    }

    fun setStartActions(vararg actions: Action) {
        binding.layoutActionsStart.removeAllViews()
        binding.layoutActionsStart.isVisible = actions.isNotEmpty()
        actions.forEach { action ->
            AppCompatImageButton(context).apply {
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.MATCH_PARENT
                )
                setMeasuredDimension(measuredHeight, measuredHeight)
                setPadding(dimen(R.dimen.margin_default))
                scaleType = ImageView.ScaleType.CENTER_INSIDE
                setColorFilter(color(action.tintRes), PorterDuff.Mode.SRC_IN)
                setImageResource(action.iconRes)
                setOnSingleClickListener {
                    action.onClick?.invoke()
                }
            }.let(binding.layoutActionsStart::addView)
        }
    }

    fun setEndActions(vararg actions: Action) {
        binding.layoutActionsEnd.removeAllViews()
        binding.layoutActionsEnd.isVisible = actions.isNotEmpty()
        actions.forEach { action ->
            AppCompatImageButton(context).apply {
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.MATCH_PARENT
                )
                setMeasuredDimension(measuredHeight, measuredHeight)
                setPadding(dimen(R.dimen.margin_default))
                scaleType = ImageView.ScaleType.CENTER_INSIDE
                setColorFilter(color(action.tintRes), PorterDuff.Mode.SRC_IN)
                setImageResource(action.iconRes)
                setOnSingleClickListener {
                    action.onClick?.invoke()
                }
            }.let(binding.layoutActionsEnd::addView)
        }
    }

    data class Action(
        @DrawableRes val iconRes: Int,
        @ColorRes val tintRes: Int = R.color.onPrimary,
        val onClick: (() -> Unit)? = null
    )
}
