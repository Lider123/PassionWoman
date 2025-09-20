package ru.babaetskv.passionwoman.app.presentation.feature.demopresets

import androidx.lifecycle.LiveData
import ru.babaetskv.passionwoman.app.presentation.base.IViewModel

interface DemoPresetsViewModel : IViewModel {
    val presetsLiveData: LiveData<List<DemoPreset>>

    fun onPresetChanged(preset: DemoPreset)
    fun onApplyPressed()
}
