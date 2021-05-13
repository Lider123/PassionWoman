package ru.babaetskv.passionwoman.app

import com.github.terrakok.cicerone.androidx.FragmentScreen
import ru.babaetskv.passionwoman.app.presentation.feature.catalog.CatalogFragment
import ru.babaetskv.passionwoman.app.presentation.feature.category.CategoryFragment
import ru.babaetskv.passionwoman.app.presentation.feature.splash.SplashFragment
import ru.babaetskv.passionwoman.domain.model.Category

object Screens {

    fun splash() = FragmentScreen {
        SplashFragment.create()
    }

    fun catalog() = FragmentScreen {
        CatalogFragment.create()
    }

    fun category(category: Category) = FragmentScreen {
        CategoryFragment.create(category)
    }
}