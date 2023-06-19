package ru.babaetskv.passionwoman.app.presentation

import android.content.Intent
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import androidx.fragment.app.Fragment
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.androidx.AppNavigator
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.babaetskv.passionwoman.app.R
import ru.babaetskv.passionwoman.app.navigation.MainAppNavigator
import ru.babaetskv.passionwoman.app.presentation.base.BaseActivity
import ru.babaetskv.passionwoman.app.presentation.base.ViewComponent
import ru.babaetskv.passionwoman.app.presentation.event.Event
import ru.babaetskv.passionwoman.app.utils.notifier.AlertSnackbarFactory
import ru.babaetskv.passionwoman.app.utils.notifier.AlertMessage
import java.util.*

// TODO: fix status and navigation bars color
class MainActivity : BaseActivity<MainViewModel>() {
    private val navigatorHolder: NavigatorHolder by inject()
    private val currentFragment: ViewComponent<*>?
        get() = supportFragmentManager.findFragmentById(R.id.container) as? ViewComponent<*>
    private val navigator: AppNavigator by lazy {
        MainAppNavigator(this, R.id.container)
    }
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
    private var savedFragments = mutableListOf<Fragment>()

    override val contentViewRes: Int = R.layout.activity_main
    override val viewModel: MainViewModel by viewModel<MainViewModelImpl>()
    override val applyInsets: Boolean = false
    override val screenName: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        installAppSplashScreen {
            !viewModel.appIsReady
        }

        super.onCreate(savedInstanceState)
        savedInstanceState ?: run {
            viewModel.handleIntent(intent, true)
        }
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        savedFragments.clear()
        savedFragments.addAll(supportFragmentManager.fragments)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        supportFragmentManager.beginTransaction()
            .apply {
                savedFragments.forEach {
                    add(it, null)
                }
            }
            .commit()
        savedFragments.clear()
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        navigatorHolder.setNavigator(navigator)
    }

    override fun onPause() {
        navigatorHolder.removeNavigator()
        super.onPause()
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        viewModel.handleIntent(intent, false)
    }

    override fun onBackPressed() {
        currentFragment?.onBackPressed() ?: super.onBackPressed()
    }

    override fun onEvent(event: Event) {
        when (event) {
            is MainViewModel.ShowAlertMessageEvent -> handleAlertMessage(event.message)
            else -> super.onEvent(event)
        }
    }

    private fun onSnackbarVisibilityChanged(isVisible: Boolean) {
        snackbarIsVisible = isVisible
        if (!isVisible) alertMessageQueue.poll()?.let {
            showAlertMessage(it)
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
