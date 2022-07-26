package ru.babaetskv.passionwoman.app.utils

import ru.babaetskv.passionwoman.app.R
import ru.babaetskv.passionwoman.app.presentation.feature.demopresets.DemoPreset
import ru.babaetskv.passionwoman.data.api.BaseApiImpl
import ru.babaetskv.passionwoman.domain.preferences.AppPreferences
import ru.babaetskv.passionwoman.domain.preferences.AuthPreferences

fun AppPreferences.toDemoPresets() = listOf(
    DemoPreset.SingleDemoPreset(
        DemoPreset.PreferenceId.SHOW_ONBOARDING,
        R.string.demo_presets_show_onboarding,
        !onboardingShowed
    )
)

fun AppPreferences.applyDemoPresets(presets: List<DemoPreset>) {
    presets.find { it.id == DemoPreset.PreferenceId.SHOW_ONBOARDING }?.let {
        onboardingShowed = !(it.value as Boolean)
    }
}

fun AuthPreferences.toDemoPresets() = listOf(
    DemoPreset.SingleDemoPreset(
        DemoPreset.PreferenceId.PROFILE_EXISTS,
        R.string.demo_presets_profile_exists,
        profileIsFilled
    ),
    DemoPreset.MultiDemoPreset(
        DemoPreset.PreferenceId.AUTHENTICATION,
        R.string.demo_presets_authentication,
        authType,
        AuthPreferences.AuthType.values().map { Pair(it.getTitleRes(), it) }
    )
)

fun AuthPreferences.applyDemoPresets(presets: List<DemoPreset>) {
    presets.find { it.id == DemoPreset.PreferenceId.AUTHENTICATION }?.let {
        authType = it.value as AuthPreferences.AuthType
        authToken = if (authType == AuthPreferences.AuthType.AUTHORIZED) {
            BaseApiImpl.TOKEN
        } else ""
    }
    presets.find { it.id == DemoPreset.PreferenceId.PROFILE_EXISTS }?.let {
        profileIsFilled = it.value as Boolean
    }
}

fun AuthPreferences.AuthType.getTitleRes(): Int = when (this) {
    AuthPreferences.AuthType.AUTHORIZED -> R.string.demo_presets_authorized
    AuthPreferences.AuthType.GUEST -> R.string.demo_presets_guest
    AuthPreferences.AuthType.NONE -> R.string.demo_presets_none
}
