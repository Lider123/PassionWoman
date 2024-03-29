package ru.babaetskv.passionwoman.app.navigation

import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction
import com.github.terrakok.cicerone.Command
import com.github.terrakok.cicerone.androidx.AppNavigator
import com.github.terrakok.cicerone.androidx.FragmentScreen
import ru.babaetskv.passionwoman.app.R
import ru.babaetskv.passionwoman.app.navigation.commands.OpenBottomSheet
import ru.babaetskv.passionwoman.app.presentation.base.ContainerBottomSheetFragment
import ru.babaetskv.passionwoman.app.utils.hideKeyboard

class MainAppNavigator(
    activity: FragmentActivity,
    @IdRes containerId: Int
) : AppNavigator(activity, containerId) {

    override fun setupFragmentTransaction(
        screen: FragmentScreen,
        fragmentTransaction: FragmentTransaction,
        currentFragment: Fragment?,
        nextFragment: Fragment
    ) {
        fragmentTransaction.setCustomAnimations(
            R.anim.fragment_fade_in,
            R.anim.fragment_fade_out,
            R.anim.fragment_fade_in,
            R.anim.fragment_fade_out
        )
    }

    override fun applyCommands(commands: Array<out Command>) {
        activity.hideKeyboard()
        super.applyCommands(commands)
    }

    override fun applyCommand(command: Command) {
        when (command) {
            is OpenBottomSheet -> openIntoBottomSheet(command.screen)
            else -> super.applyCommand(command)
        }
    }

    private fun openIntoBottomSheet(screen: FragmentScreen) {
        val bottomSheetFragment = ContainerBottomSheetFragment.create()
        val transaction = fragmentManager.beginTransaction()
        transaction.setReorderingAllowed(true)
        setupFragmentTransaction(
            screen,
            transaction,
            fragmentManager.findFragmentById(containerId),
            bottomSheetFragment
        )
        transaction.addToBackStack(screen.screenKey)
        localStackCopy.add(screen.screenKey)
        bottomSheetFragment.show(transaction, screen.screenKey)
        val contentFragment = screen.createFragment(fragmentFactory)
        bottomSheetFragment.setContent(contentFragment)
    }
}