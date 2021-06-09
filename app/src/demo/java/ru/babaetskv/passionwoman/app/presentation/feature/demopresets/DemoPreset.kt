package ru.babaetskv.passionwoman.app.presentation.feature.demopresets

import androidx.annotation.StringRes

sealed class DemoPreset {
    abstract val id: PreferenceId
    abstract val titleRes: Int
    abstract var value: Any

    data class SingleDemoPreset(
        override val id: PreferenceId,
        @StringRes override val titleRes: Int,
        override var value: Any
    ) : DemoPreset()

    data class MultiDemoPreset(
        override val id: PreferenceId,
        @StringRes override val titleRes: Int,
        override var value: Any,
        val availableValuesWithTitles: List<Pair<Int, Any>>
    ) : DemoPreset()

    enum class PreferenceId {
        SHOW_ONBOARDING, AUTHENTICATION, PROFILE_EXISTS
    }
}
