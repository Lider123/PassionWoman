package ru.babaetskv.passionwoman.app.presentation

import android.graphics.Rect
import android.os.Build
import androidx.lifecycle.lifecycleScope
import com.github.terrakok.cicerone.NavigatorHolder
import kotlinx.coroutines.flow.collect
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.babaetskv.passionwoman.app.R
import ru.babaetskv.passionwoman.app.navigation.Screens
import ru.babaetskv.passionwoman.app.navigation.AppRouter
import ru.babaetskv.passionwoman.app.navigation.MainAppNavigator
import ru.babaetskv.passionwoman.app.presentation.base.BaseActivity
import ru.babaetskv.passionwoman.app.presentation.base.ViewComponent
import ru.babaetskv.passionwoman.app.utils.notifier.AlertSnackbarFactory
import ru.babaetskv.passionwoman.app.utils.notifier.AlertMessage
import java.util.*

class MainActivity : BaseActivity<MainViewModel, MainViewModel.Router>() {
    private val navigatorHolder: NavigatorHolder by inject()
    private val router: AppRouter by inject()
    private val currentFragment: ViewComponent<*, *>?
        get() = supportFragmentManager.findFragmentById(R.id.container) as? ViewComponent<*, *>
    private val navigator = MainAppNavigator(this, R.id.container)
    private val snackbarFactory: AlertSnackbarFactory by lazy {
        AlertSnackbarFactory(this, componentView, ::onSnackbarVisibilityChanged)
    }
    private var snackbarIsVisible = false
    private val statusBarHeight: Int
        get() {
            val rectangle = Rect()
            window.decorView.getWindowVisibleDisplayFrame(rectangle)
            return rectangle.top
        }
    private val alertMessageQueue: Queue<AlertMessage> = LinkedList()

    override val contentViewRes: Int = R.layout.activity_main
    override val viewModel: MainViewModel by viewModel()
    override val applyInsets: Boolean = false
    override val screenName: String = ""

    override fun onResumeFragments() {
        super.onResumeFragments()
        navigatorHolder.setNavigator(navigator)
    }

    override fun onPause() {
        navigatorHolder.removeNavigator()
        super.onPause()
    }

    override fun onBackPressed() {
        currentFragment?.onBackPressed() ?: super.onBackPressed()
    }

    override fun initObservers() {
        super.initObservers()
        lifecycleScope.launchWhenResumed {
            viewModel.eventBus.collect(::handleEvent)
        }
    }

    override fun handleRouterEvent(event: MainViewModel.Router) {
        super.handleRouterEvent(event)
        when (event) {
            MainViewModel.Router.SplashScreen -> router.newRootScreen(Screens.splash())
        }
    }

    private fun onSnackbarVisibilityChanged(isVisible: Boolean) {
        snackbarIsVisible = isVisible
        if (!isVisible) alertMessageQueue.poll()?.let {
            showAlertMessage(it)
        }
    }

    private fun handleEvent(event: MainViewModel.Event) {
        when (event) {
            is MainViewModel.Event.ShowAlertMessage -> handleAlertMessage(event.message)
        }
    }

    private fun handleAlertMessage(message: AlertMessage) {
        if (!snackbarIsVisible) {
            showAlertMessage(message)
        } else if (message.isImportant) {
            alertMessageQueue.add(message)
        }
    }

    private fun showAlertMessage(message: AlertMessage) {
        val topInset: Int = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            statusBarHeight
        } else 0
        snackbarFactory.create(topInset, message)
            .show()
    }
}
