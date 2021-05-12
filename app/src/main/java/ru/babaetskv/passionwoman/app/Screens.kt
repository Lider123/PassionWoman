package ru.babaetskv.passionwoman.app

import com.github.terrakok.cicerone.androidx.FragmentScreen
import ru.babaetskv.passionwoman.app.presentation.feature.catalog.CatalogFragment
import ru.babaetskv.passionwoman.app.presentation.feature.splash.SplashFragment

object Screens {

    fun splash() = FragmentScreen {
        SplashFragment.create()
    }

    fun catalog() = FragmentScreen {
        CatalogFragment.create()
    }
}