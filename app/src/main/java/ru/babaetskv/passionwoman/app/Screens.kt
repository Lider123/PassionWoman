package ru.babaetskv.passionwoman.app

import com.github.terrakok.cicerone.androidx.FragmentScreen
import ru.babaetskv.passionwoman.app.presentation.feature.category.CategoryFragment
import ru.babaetskv.passionwoman.app.presentation.feature.auth.AuthFragment
import ru.babaetskv.passionwoman.app.presentation.feature.auth.signup.EditProfileFragment
import ru.babaetskv.passionwoman.app.presentation.feature.navigation.NavigationFragment
import ru.babaetskv.passionwoman.app.presentation.feature.onboarding.OnboardingFragment
import ru.babaetskv.passionwoman.app.presentation.feature.splash.SplashFragment
import ru.babaetskv.passionwoman.domain.model.Category
import ru.babaetskv.passionwoman.domain.model.Profile

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
        CategoryFragment.create(category)
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
}