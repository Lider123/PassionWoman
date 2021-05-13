package ru.babaetskv.passionwoman.app.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.github.terrakok.cicerone.Command
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.androidx.AppNavigator
import com.github.terrakok.cicerone.androidx.FragmentScreen
import org.koin.android.ext.android.inject
import ru.babaetskv.passionwoman.app.R
import ru.babaetskv.passionwoman.app.Screens
import ru.babaetskv.passionwoman.app.presentation.base.BaseActivity

class MainActivity : BaseActivity() {
    private val navigatorHolder: NavigatorHolder by inject()

    private val navigator = object : AppNavigator(this, R.id.container) {

        override fun setupFragmentTransaction(
            screen: FragmentScreen,
            fragmentTransaction: FragmentTransaction,
            currentFragment: Fragment?,
            nextFragment: Fragment
        ) {
            fragmentTransaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
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
}
