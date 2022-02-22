package ru.babaetskv.passionwoman.app.presentation.view

import android.content.Context
import android.util.AttributeSet
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.google.android.material.snackbar.ContentViewCallback
import ru.babaetskv.passionwoman.app.R
import ru.babaetskv.passionwoman.app.databinding.ViewAlertBinding
import ru.babaetskv.passionwoman.app.utils.color
import ru.babaetskv.passionwoman.app.utils.inflateLayout

class AlertView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), ContentViewCallback {
    private val binding: ViewAlertBinding

    var message: CharSequence
        get() = binding.tvMessage.text
        set(value) {
            binding.tvMessage.run {
                text = value
                isVisible = value.isNotBlank()
            }
        }

    init {
        inflateLayout(R.layout.view_alert, true)
        binding = ViewAlertBinding.bind(this)
        clipToPadding = false
    }

    override fun animateContentIn(delay: Int, duration: Int) = Unit

    override fun animateContentOut(delay: Int, duration: Int) = Unit

    fun setIcon(@DrawableRes iconRes: Int?) {
        binding.ivIcon.run {
            isVisible = iconRes?.let {
                setImageResource(it)
                true
            } ?: false
        }
    }

    fun setAlertColor(@ColorRes colorRes: Int) {
        binding.container.setCardBackgroundColor(color(colorRes))
    }
}
