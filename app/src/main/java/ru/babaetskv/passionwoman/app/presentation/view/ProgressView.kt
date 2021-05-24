package ru.babaetskv.passionwoman.app.presentation.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.FrameLayout
import ru.babaetskv.passionwoman.app.R
import ru.babaetskv.passionwoman.app.databinding.ViewProgressBinding


class ProgressView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    private val binding: ViewProgressBinding
    private val blinkingAnimation: Animation by lazy {
        AlphaAnimation(0.35f, 1f).apply {
            duration = 750
            repeatMode = Animation.REVERSE
            repeatCount = Animation.INFINITE
        }
    }

    init {
        LayoutInflater.from(context).inflate(R.layout.view_progress, this)
        binding = ViewProgressBinding.bind(this)
    }

    override fun onVisibilityChanged(changedView: View, visibility: Int) {
        super.onVisibilityChanged(changedView, visibility)
        if (visibility == View.VISIBLE) startAnimation(blinkingAnimation) else clearAnimation()
    }
}