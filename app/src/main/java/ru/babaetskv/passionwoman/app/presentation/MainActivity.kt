package ru.babaetskv.passionwoman.app.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.lifecycleScope
import com.github.terrakok.cicerone.Command
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.androidx.AppNavigator
import com.github.terrakok.cicerone.androidx.FragmentScreen
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import org.koin.android.ext.android.inject
import ru.babaetskv.passionwoman.app.R
import ru.babaetskv.passionwoman.app.Screens
import ru.babaetskv.passionwoman.app.presentation.base.BaseActivity
import ru.babaetskv.passionwoman.app.presentation.base.BaseFragment
import ru.babaetskv.passionwoman.app.utils.notifier.AlertToast
import ru.babaetskv.passionwoman.app.utils.notifier.Message
import ru.babaetskv.passionwoman.app.utils.notifier.Notifier

class MainActivity : BaseActivity() {
    private val navigatorHolder: NavigatorHolder by inject()
    private val notifier: Notifier by inject()

    private var alertChannel: ReceiveChannel<Message>? = null
    private val currentFragment: BaseFragment<*, *>?
        get() = supportFragmentManager.findFragmentById(R.id.container) as? BaseFragment<*, *>
    private val navigator = object : AppNavigator(this, R.id.container) {

        override fun setupFragmentTransaction(
            screen: FragmentScreen,
            fragmentTransaction: FragmentTransaction,
            currentFragment: Fragment?,
            nextFragment: Fragment
        ) {
            fragmentTransaction.setCustomAnimations(R.anim.fragment_fade_in, R.anim.fragment_fade_out, R.anim.fragment_fade_in, R.anim.fragment_fade_out)
        }

        override fun applyCommands(commands: Array<out Command>) {
            hideKeyboard()
            super.applyCommands(commands)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        router.newRootScreen(Screens.splash())
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        navigatorHolder.setNavigator(navigator)
    }

    override fun onPause() {
        navigatorHolder.removeNavigator()
        super.onPause()
    }

    override fun onStart() {
        super.onStart()
        subscribeOnAlerts()
    }

    override fun onStop() {
        unsubscribeFromAlerts()
        super.onStop()
    }

    override fun onBackPressed() {
        currentFragment?.onBackPressed() ?: super.onBackPressed()
    }

    private fun subscribeOnAlerts() {
        alertChannel = notifier.subscribe()
        lifecycleScope.launchWhenResumed {
            alertChannel!!.consumeAsFlow().collect(::onNextAlertMessage)
        }
    }

    private fun unsubscribeFromAlerts() {
        alertChannel?.cancel()
        alertChannel = null
    }

    private fun onNextAlertMessage(message: Message) {
        if (message.text.isBlank()) return

        AlertToast.create(this, message).show()
    }
}
