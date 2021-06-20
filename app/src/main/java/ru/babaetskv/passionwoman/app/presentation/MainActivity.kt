package ru.babaetskv.passionwoman.app.presentation

import androidx.lifecycle.lifecycleScope
import com.github.terrakok.cicerone.NavigatorHolder
import kotlinx.coroutines.flow.collect
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.babaetskv.passionwoman.app.R
import ru.babaetskv.passionwoman.app.Screens
import ru.babaetskv.passionwoman.app.navigation.MainAppNavigator
import ru.babaetskv.passionwoman.app.presentation.base.BackButtonListener
import ru.babaetskv.passionwoman.app.presentation.base.BaseActivity
import ru.babaetskv.passionwoman.app.utils.notifier.AlertToast

class MainActivity : BaseActivity<MainViewModel, MainViewModel.Router>() {
    private val navigatorHolder: NavigatorHolder by inject()

    private val currentFragment: BackButtonListener?
        get() = supportFragmentManager.findFragmentById(R.id.container) as? BackButtonListener
    private val navigator = MainAppNavigator(this, R.id.container)

    override val contentViewRes: Int = R.layout.activity_main
    override val viewModel: MainViewModel by viewModel()

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

    private fun handleEvent(event: MainViewModel.Event) {
        when (event) {
            is MainViewModel.Event.ShowAlertMessage -> {
                AlertToast.create(this, event.message)
                    .show()
            }
        }
    }
}
