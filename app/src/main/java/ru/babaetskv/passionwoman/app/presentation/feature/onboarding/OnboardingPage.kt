package ru.babaetskv.passionwoman.app.presentation.feature.onboarding

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class OnboardingPage(
    @DrawableRes val bannerRes: Int,
    @StringRes val messageRes: Int,
    @StringRes val actionRes: Int? = null,
    val actionCallback: (() -> Unit)? = null
)
