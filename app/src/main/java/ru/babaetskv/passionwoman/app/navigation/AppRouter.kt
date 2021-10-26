package ru.babaetskv.passionwoman.app.navigation

import com.github.terrakok.cicerone.Router
import ru.babaetskv.passionwoman.app.navigation.commands.OpenBottomSheet

class AppRouter : Router() {

    fun openBottomSheet(screen: BottomSheetDialogFragmentScreen) {
        executeCommands(OpenBottomSheet(screen))
    }
}
