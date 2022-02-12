package ru.babaetskv.passionwoman.app.presentation.feature.demopresets

import android.content.res.Resources
import androidx.lifecycle.MutableLiveData
import ru.babaetskv.passionwoman.app.R
import ru.babaetskv.passionwoman.app.presentation.base.BaseViewModel
import ru.babaetskv.passionwoman.app.presentation.event.RouterEvent
import ru.babaetskv.passionwoman.app.presentation.base.ViewModelDependencies
import ru.babaetskv.passionwoman.app.utils.applyDemoPresets
import ru.babaetskv.passionwoman.app.utils.toDemoPresets
import ru.babaetskv.passionwoman.domain.preferences.AppPreferences
import ru.babaetskv.passionwoman.domain.preferences.AuthPreferences
import ru.babaetskv.passionwoman.domain.usecase.GetProfileUseCase
import ru.babaetskv.passionwoman.domain.usecase.UpdateProfileUseCase

class DemoPresetsViewModelImpl(
    private val appPrefs: AppPreferences,
    private val authPrefs: AuthPreferences,
    private val getProfileUseCase: GetProfileUseCase,
    private val updateProfileUseCase: UpdateProfileUseCase,
    private val resources: Resources,
    dependencies: ViewModelDependencies
) : BaseViewModel<DemoPresetsViewModelImpl.Router>(dependencies), DemoPresetsViewModel {
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
            if (authPrefs.profileIsFilled) {
                val profile = getProfileUseCase.execute()
                profile.copy(
                    name = resources.getString(R.string.demo_name),
                    surname = resources.getString(R.string.demo_surname)
                ).also {
                    updateProfileUseCase.execute(it)
                }
            }
            navigateTo(Router.MainFlow)
        }
    }

    sealed class Router : RouterEvent {
        object MainFlow : Router()
    }
}
