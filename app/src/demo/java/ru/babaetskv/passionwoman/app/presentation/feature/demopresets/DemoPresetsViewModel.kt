package ru.babaetskv.passionwoman.app.presentation.feature.demopresets

import androidx.lifecycle.LiveData
import ru.babaetskv.passionwoman.app.presentation.base.IViewModel
import ru.babaetskv.passionwoman.app.presentation.event.Event

interface DemoPresetsViewModel : IViewModel {
    val presetsLiveData: LiveData<List<DemoPreset>>
    val splashScreenVisible: Boolean

    fun onPresetChanged(preset: DemoPreset)
    fun onApplyPressed()

    object StartMainFlowEvent : Event
}
