package ru.babaetskv.passionwoman.app.presentation.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import ru.babaetskv.passionwoman.app.R
import ru.babaetskv.passionwoman.app.presentation.SimpleKeyboardAnimator
import ru.babaetskv.passionwoman.app.presentation.view.ProgressView

abstract class BaseActivity<VM : BaseViewModel> : AppCompatActivity() {
    private val keyboardAnimator: SimpleKeyboardAnimator by lazy {
        SimpleKeyboardAnimator(window)
    }

    abstract val contentViewRes: Int
    abstract val viewModel: VM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(contentViewRes)
        initView()
        initObservers()
    }

    override fun onStart() {
        super.onStart()
        keyboardAnimator.start()
        viewModel.onStart()
    }

    override fun onResume() {
        super.onResume()
        viewModel.onResume()
    }

    override fun onPause() {
        viewModel.onPause()
        super.onPause()
    }

    override fun onStop() {
        keyboardAnimator.stop()
        viewModel.onStop()
        super.onStop()
    }

    open fun initView() = Unit

    open fun initObservers() {
        viewModel.loadingLiveData.observe(this, ::showLoading)
    }

    open fun showLoading(show: Boolean) {
        findViewById<ProgressView>(R.id.progressView)?.isVisible = show
    }
}
