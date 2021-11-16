package ru.babaetskv.passionwoman.app.navigation

import com.github.terrakok.cicerone.androidx.FragmentScreen
import ru.babaetskv.passionwoman.app.presentation.feature.contacts.ContactsFragment
import ru.babaetskv.passionwoman.app.presentation.feature.productlist.ProductListFragment
import ru.babaetskv.passionwoman.app.presentation.feature.auth.AuthFragment
import ru.babaetskv.passionwoman.app.presentation.feature.auth.signup.EditProfileFragment
import ru.babaetskv.passionwoman.app.presentation.feature.navigation.NavigationFragment
import ru.babaetskv.passionwoman.app.presentation.feature.onboarding.OnboardingFragment
import ru.babaetskv.passionwoman.app.presentation.feature.productcard.ProductCardFragment
import ru.babaetskv.passionwoman.app.presentation.feature.productlist.FavoritesFragment
import ru.babaetskv.passionwoman.app.presentation.feature.productlist.filters.FiltersFragment
import ru.babaetskv.passionwoman.app.presentation.feature.productlist.sorting.SortingFragment
import ru.babaetskv.passionwoman.app.presentation.feature.splash.SplashFragment
import ru.babaetskv.passionwoman.app.utils.deeplink.DeeplinkPayload
import ru.babaetskv.passionwoman.domain.model.*
import ru.babaetskv.passionwoman.domain.model.filters.Filter

object Screens {

    fun splash(payload: DeeplinkPayload?) = FragmentScreen {
        SplashFragment.create(payload)
    }

    fun onboarding() = FragmentScreen {
        OnboardingFragment.create()
    }

    fun navigation(payload: DeeplinkPayload?) = FragmentScreen {
        NavigationFragment.create(payload)
    }

    fun category(category: Category) = FragmentScreen {
        ProductListFragment.create(
            categoryId = category.id,
            title = category.name,
            filters = listOf(),
            sorting = Sorting.DEFAULT,
            actionsAvailable = true
        )
    }

    fun productList(title: String, filters: List<Filter>, sorting: Sorting) = FragmentScreen {
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

    fun productCard(productId: String) = FragmentScreen {
        ProductCardFragment.create(productId)
    }

    fun sorting(sorting: Sorting) = BottomSheetDialogFragmentScreen {
        SortingFragment.create(sorting)
    }

    fun contacts() = BottomSheetDialogFragmentScreen {
        ContactsFragment.create()
    }

    fun filters(
        categoryId: String?,
        filters: List<Filter>,
        productsCount: Int
    ) =
        BottomSheetDialogFragmentScreen {
            FiltersFragment.create(categoryId, filters, productsCount)
        }
}