package ru.babaetskv.passionwoman.app.presentation.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import ru.babaetskv.passionwoman.app.R
import ru.babaetskv.passionwoman.app.databinding.ViewStubBinding
import ru.babaetskv.passionwoman.app.utils.load
import ru.babaetskv.passionwoman.app.utils.setOnSingleClickListener
import ru.babaetskv.passionwoman.app.utils.viewBinding

class StubView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    private val binding = viewBinding(ViewStubBinding::inflate, true)
    private var backButtonListener: ((View) -> Unit)? = null
    private var actionButtonListener: ((View) -> Unit)? = null

    var message: String
        get() = binding.tvMessage.text.toString()
        set(value) {
            binding.tvMessage.text = value
        }
    var action: String
        get() = binding.btnAction.text.toString()
        set(value) {
            binding.btnAction.text = value
        }
    var isBackButtonVisible: Boolean
        get() = binding.btnBack.isVisible
        set(value) {
            binding.btnBack.isVisible = value
        }
    var isActionButtonVisible: Boolean
        get() = binding.btnAction.isVisible
        set(value) {
            binding.btnAction.isVisible = value
        }

    init {
        context.theme.obtainStyledAttributes(attrs, R.styleable.StubView, 0, 0).apply {
            try {
                action = getString(R.styleable.StubView_sv_action) ?: ""
                message = getString(R.styleable.StubView_sv_message) ?: ""
                isBackButtonVisible = getBoolean(R.styleable.StubView_sv_isBackVisible, true)
                isActionButtonVisible = getBoolean(R.styleable.StubView_sv_isActionVisible, true)
            } finally {
                recycle()
            }
        }
        binding.run {
            ivBanner.load(R.drawable.banner_in_development)
            btnBack.setOnSingleClickListener {
                backButtonListener?.invoke(it)
            }
            btnAction.setOnSingleClickListener {
                actionButtonListener?.invoke(it)
            }
        }
    }

    fun setBackButtonListener(listener: ((View) -> Unit)?) {
        backButtonListener = listener
    }

    fun setActionButtonListener(listener: ((View) -> Unit)?) {
        actionButtonListener = listener
    }
}
