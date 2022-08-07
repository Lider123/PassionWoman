package ru.babaetskv.passionwoman.app.presentation.base

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import ru.babaetskv.passionwoman.app.R
import ru.babaetskv.passionwoman.app.presentation.SimpleKeyboardAnimator
import ru.babaetskv.passionwoman.app.presentation.event.RouterEvent
import ru.babaetskv.passionwoman.app.utils.setInsetsListener

abstract class BaseActivity<VM : IViewModel, TRouterEvent : RouterEvent> :
    AppCompatActivity(),
    ViewComponent<VM, TRouterEvent> {
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

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(resources.getBoolean(R.bool.portrait_mode_only)) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
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

    override fun handleLogInRouterEvent(event: RouterEvent.LogIn) = Unit

    protected fun installAppSplashScreen(condition: SplashScreen.KeepOnScreenCondition? = null): SplashScreen =
        installSplashScreen().apply {
            condition?.run(::setKeepOnScreenCondition)
            setOnExitAnimationListener { splashScreenView ->
                val fadeOut = ObjectAnimator.ofFloat(splashScreenView.iconView, View.ALPHA, 1f, 0f).apply {
                    interpolator = LinearInterpolator()
                    duration = 500L
                    doOnEnd { splashScreenView.remove() }
                }
                fadeOut.start()
            }
        }
}
