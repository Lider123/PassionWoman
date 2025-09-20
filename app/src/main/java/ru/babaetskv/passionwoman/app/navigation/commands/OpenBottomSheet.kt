package ru.babaetskv.passionwoman.app.navigation.commands

import com.github.terrakok.cicerone.Command
import com.github.terrakok.cicerone.androidx.FragmentScreen

data class OpenBottomSheet(
    val screen: FragmentScreen
) : Command
