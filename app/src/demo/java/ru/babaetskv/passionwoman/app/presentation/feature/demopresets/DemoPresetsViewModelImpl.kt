package ru.babaetskv.passionwoman.app.presentation.feature.demopresets

import android.content.res.Resources
import androidx.lifecycle.MutableLiveData
import ru.babaetskv.passionwoman.app.R
import ru.babaetskv.passionwoman.app.navigation.ScreenProvider
import ru.babaetskv.passionwoman.app.presentation.base.BaseViewModel
import ru.babaetskv.passionwoman.app.presentation.base.ViewModelDependencies
import ru.babaetskv.passionwoman.app.utils.applyDemoPresets
import ru.babaetskv.passionwoman.app.utils.toDemoPresets
import ru.babaetskv.passionwoman.domain.preferences.AppPreferences
import ru.babaetskv.passionwoman.domain.preferences.AuthPreferences
import ru.babaetskv.passionwoman.domain.usecase.GetProfileUseCase
import ru.babaetskv.passionwoman.domain.usecase.InitDatabaseUseCase
import ru.babaetskv.passionwoman.domain.usecase.RegisterPushTokenUseCase
import ru.babaetskv.passionwoman.domain.usecase.UpdateProfileUseCase
import ru.babaetskv.passionwoman.domain.usecase.base.UseCase.Companion.execute

class DemoPresetsViewModelImpl(
    private val args: DemoPresetsFragment.Args,
    private val appPrefs: AppPreferences,
    private val authPrefs: AuthPreferences,
    private val getProfileUseCase: GetProfileUseCase,
    private val updateProfileUseCase: UpdateProfileUseCase,
    private val initDatabaseUseCase: InitDatabaseUseCase,
    private val registerPushTokenUseCase: RegisterPushTokenUseCase,
    private val resources: Resources,
    dependencies: ViewModelDependencies
) : BaseViewModel(dependencies), DemoPresetsViewModel {
    override val presetsLiveData = MutableLiveData(
        appPrefs.toDemoPresets() + authPrefs.toDemoPresets()
    )

    override fun onPresetChanged(preset: DemoPreset) {
        presetsLiveData.value?.find {it.id == preset.id }?.value = preset.value
    }

    override fun onApplyPressed() {
        launchWithLoading {
            val presets = presetsLiveData.value!!
            appPrefs.applyDemoPresets(presets)
            authPrefs.applyDemoPresets(presets)
            if (authPrefs.authType == AuthPreferences.AuthType.AUTHORIZED) {
                registerPushTokenUseCase.execute()
                val profile = getProfileUseCase.execute()
                profile.copy(
                    name = if (authPrefs.profileIsFilled) resources.getString(R.string.demo_name) else "",
                    surname = if (authPrefs.profileIsFilled) resources.getString(R.string.demo_surname) else ""
                ).let {
                    updateProfileUseCase.execute(it)
                }
            }
            if (args.onStart) {
                appPrefs.demoOnStartShowed = true
                navigateOnStart()
            } else router.exit()
        }
    }

    private fun navigateOnStart() {
        when {
            !appPrefs.onboardingShowed -> router.newRootScreen(ScreenProvider.onboarding())
            authPrefs.authType == AuthPreferences.AuthType.NONE -> {
                router.newRootScreen(ScreenProvider.auth(true))
            }
            authPrefs.authType == AuthPreferences.AuthType.AUTHORIZED
                    && !authPrefs.profileIsFilled -> launchWithLoading {
                val profile = getProfileUseCase.execute()
                router.newRootScreen(ScreenProvider.signUp(profile, true))
            }
            else -> router.newRootScreen(ScreenProvider.navigation(null))
        }
    }
}
