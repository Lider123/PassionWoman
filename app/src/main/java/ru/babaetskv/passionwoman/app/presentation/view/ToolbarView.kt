package ru.babaetskv.passionwoman.app.presentation.view

import android.content.Context
import android.graphics.PorterDuff
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.TextViewCompat
import ru.babaetskv.passionwoman.app.R
import ru.babaetskv.passionwoman.app.databinding.ViewToolbarBinding
import ru.babaetskv.passionwoman.app.utils.setOnSingleClickListener

class ToolbarView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    private val binding: ViewToolbarBinding
    private var onStartClick: ((v: View) -> Unit)? = null
    private var onEndClick: ((v: View) -> Unit)? = null

    var title: String
        get() = binding.tvTitle.text.toString()
        set(value) {
            binding.tvTitle.text = value
        }
    var btnStartVisible: Boolean
        get() = binding.btnActionStart.isVisible
        set(value) {
            binding.btnActionStart.isVisible = value
        }
    var btnEndVisible: Boolean
        get() = binding.btnActionEnd.isVisible
        set(value) {
            binding.btnActionEnd.isVisible = value
        }

    init {
        LayoutInflater.from(context).inflate(R.layout.view_toolbar, this)
        binding = ViewToolbarBinding.bind(this)
        context.theme.obtainStyledAttributes(attrs, R.styleable.ToolbarView, 0, 0).apply {
            try {
                title = getString(R.styleable.ToolbarView_tv_title) ?: context.getString(R.string.app_name)
                getResourceId(R.styleable.ToolbarView_tv_actionStartSrc, -1).let {
                    if (it == -1) null else it
                }.let {
                    setActionStart(it)
                }
                getResourceId(R.styleable.ToolbarView_tv_actionEndSrc, -1).let {
                    if (it == -1) null else it
                }.let {
                    setActionEnd(it)
                }
                getResourceId(R.styleable.ToolbarView_tv_actionStartTint, -1).let {
                    if (it == -1) null else it
                }.let {
                    setActionStartTint(it)
                }
                getResourceId(R.styleable.ToolbarView_tv_actionEndTint, -1).let {
                    if (it == -1) null else it
                }.let {
                    setActionEndTint(it)
                }
                binding.tvTitle.textAlignment =
                    getInteger(R.styleable.ToolbarView_android_textAlignment, TEXT_ALIGNMENT_CENTER)
                btnStartVisible = getBoolean(R.styleable.ToolbarView_tv_actionStartVisible, false)
                btnEndVisible = getBoolean(R.styleable.ToolbarView_tv_actionEndVisible, false)
                getResourceId(R.styleable.ToolbarView_tv_titleTextAppearance, -1).let {
                    if (it == -1) null else it
                }?.let {
                    TextViewCompat.setTextAppearance(binding.tvTitle, it)
                }
            } finally {
                recycle()
            }
        }
        binding.run {
            btnActionStart.setOnSingleClickListener {
                onStartClick?.invoke(it)
            }
            btnActionEnd.setOnSingleClickListener {
                onEndClick?.invoke(it)
            }
        }
    }

    fun setActionStart(@DrawableRes drawableRes: Int?) {
        drawableRes?.let { binding.btnActionStart.setImageResource(it) }
    }

    fun setActionEnd(@DrawableRes drawableRes: Int?) {
        drawableRes?.let { binding.btnActionEnd.setImageResource(it) }
    }

    fun setActionStartTint(@ColorRes colorRes: Int?) {
        colorRes?.let {
            binding.btnActionStart.setColorFilter(
                ContextCompat.getColor(context, it),
                PorterDuff.Mode.SRC_IN
            )
        }
    }

    fun setActionEndTint(@ColorRes colorRes: Int?) {
        colorRes?.let {
            binding.btnActionEnd.setColorFilter(
                ContextCompat.getColor(context, it),
                PorterDuff.Mode.SRC_IN
            )
        }
    }

    fun setOnStartClickListener(listener: ((v: View) -> Unit)?) {
        onStartClick = listener
    }

    fun setOnEndClickListener(listener: ((v: View) -> Unit)?) {
        onEndClick = listener
    }
}
