package ru.babaetskv.passionwoman.app.presentation.base

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import ru.babaetskv.passionwoman.app.presentation.SimpleKeyboardAnimator
import ru.babaetskv.passionwoman.app.presentation.event.RouterEvent
import ru.babaetskv.passionwoman.app.utils.setInsetsListener

abstract class BaseActivity<VM, TRouterEvent : RouterEvent> :
    AppCompatActivity(),
    ViewComponent<VM, TRouterEvent>
    where VM : BaseViewModel<TRouterEvent> {
    private val keyboardAnimator: SimpleKeyboardAnimator by lazy {
        SimpleKeyboardAnimator(window)
    }

    override val componentView: View
        get() = findViewById(android.R.id.content)
    override val componentContext: Context
        get() = this
    override val componentLifecycleScope: LifecycleCoroutineScope
        get() = lifecycleScope
    override val componentViewLifecycleOwner: LifecycleOwner
        get() = this

    abstract val contentViewRes: Int
    abstract val applyInsets: Boolean

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(contentViewRes)
        if (applyInsets) componentView.setInsetsListener()
        initViews()
        initObservers()
    }

    override fun onStart() {
        super.onStart()
        keyboardAnimator.start()
        viewModel.onStart(screenName)
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
}
