package ru.babaetskv.passionwoman.app

import com.github.terrakok.cicerone.androidx.FragmentScreen
import ru.babaetskv.passionwoman.app.presentation.feature.productlist.ProductListFragment
import ru.babaetskv.passionwoman.app.presentation.feature.auth.AuthFragment
import ru.babaetskv.passionwoman.app.presentation.feature.auth.signup.EditProfileFragment
import ru.babaetskv.passionwoman.app.presentation.feature.navigation.NavigationFragment
import ru.babaetskv.passionwoman.app.presentation.feature.onboarding.OnboardingFragment
import ru.babaetskv.passionwoman.app.presentation.feature.productcard.ProductCardFragment
import ru.babaetskv.passionwoman.app.presentation.feature.splash.SplashFragment
import ru.babaetskv.passionwoman.domain.model.*

object Screens {

    fun splash() = FragmentScreen {
        SplashFragment.create()
    }

    fun onboarding() = FragmentScreen {
        OnboardingFragment.create()
    }

    fun navigation() = FragmentScreen {
        NavigationFragment.create()
    }

    fun category(category: Category) = FragmentScreen {
        ProductListFragment.create(category.id, category.name, Filters.DEFAULT, Sorting.DEFAULT)
    }

    fun productList(title: String, filters: Filters, sorting: Sorting) = FragmentScreen {
        ProductListFragment.create(null, title, filters, sorting)
    }

    fun auth() = FragmentScreen {
        AuthFragment.create()
    }

    fun signUp(profile: Profile) = FragmentScreen {
        EditProfileFragment.create(profile, true)
    }

    fun editProfile(profile: Profile) = FragmentScreen {
        EditProfileFragment.create(profile, false)
    }

    fun productCard(product: Product) = FragmentScreen {
        ProductCardFragment.create(product)
    }
}