package ru.babaetskv.passionwoman.app.navigation.commands

import com.github.terrakok.cicerone.Command
import ru.babaetskv.passionwoman.app.navigation.BottomSheetDialogFragmentScreen

data class OpenBottomSheet(
    val screen: BottomSheetDialogFragmentScreen
) : Command
