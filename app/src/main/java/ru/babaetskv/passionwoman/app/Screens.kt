package ru.babaetskv.passionwoman.app

import com.github.terrakok.cicerone.androidx.FragmentScreen
import ru.babaetskv.passionwoman.app.navigation.BottomSheetDialogFragmentScreen
import ru.babaetskv.passionwoman.app.presentation.feature.productlist.ProductListFragment
import ru.babaetskv.passionwoman.app.presentation.feature.auth.AuthFragment
import ru.babaetskv.passionwoman.app.presentation.feature.auth.signup.EditProfileFragment
import ru.babaetskv.passionwoman.app.presentation.feature.navigation.NavigationFragment
import ru.babaetskv.passionwoman.app.presentation.feature.onboarding.OnboardingFragment
import ru.babaetskv.passionwoman.app.presentation.feature.productcard.ProductCardFragment
import ru.babaetskv.passionwoman.app.presentation.feature.productlist.FavoritesFragment
import ru.babaetskv.passionwoman.app.presentation.feature.productlist.sorting.SortingFragment
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
        ProductListFragment.create(
            categoryId = category.id,
            title = category.name,
            filters = Filters.DEFAULT,
            sorting = Sorting.DEFAULT,
            actionsAvailable = true
        )
    }

    fun productList(title: String, filters: Filters, sorting: Sorting) = FragmentScreen {
        ProductListFragment.create(
            categoryId = null,
            title = title,
            filters = filters,
            sorting = sorting,
            actionsAvailable = false
        )
    }

    fun favorites() = FragmentScreen {
        FavoritesFragment.create()
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
        ProductCardFragment.create(product.id)
    }

    fun sorting(sorting: Sorting) = BottomSheetDialogFragmentScreen {
        SortingFragment.create(sorting)
    }
}