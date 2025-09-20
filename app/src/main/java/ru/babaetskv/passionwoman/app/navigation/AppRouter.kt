package ru.babaetskv.passionwoman.app.navigation

import com.github.terrakok.cicerone.Router
import com.github.terrakok.cicerone.androidx.FragmentScreen
import ru.babaetskv.passionwoman.app.navigation.commands.OpenBottomSheet

class AppRouter : Router() {

    fun openBottomSheet(screen: FragmentScreen) {
        executeCommands(OpenBottomSheet(screen))
    }
}
