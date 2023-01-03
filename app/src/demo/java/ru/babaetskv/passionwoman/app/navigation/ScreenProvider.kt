package ru.babaetskv.passionwoman.app.navigation

import com.github.terrakok.cicerone.androidx.FragmentScreen
import ru.babaetskv.passionwoman.app.presentation.feature.demopresets.DemoPresetsFragment

object ScreenProvider : BaseScreenProvider() {

    fun demo(onStart: Boolean) = FragmentScreen {
        DemoPresetsFragment.newInstance(onStart)
    }
}
