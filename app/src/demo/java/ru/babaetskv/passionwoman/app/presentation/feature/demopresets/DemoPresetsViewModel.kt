package ru.babaetskv.passionwoman.app.presentation.feature.demopresets

import android.content.res.Resources
import androidx.lifecycle.MutableLiveData
import com.github.terrakok.cicerone.Router
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.consumeAsFlow
import ru.babaetskv.passionwoman.app.R
import ru.babaetskv.passionwoman.app.presentation.base.BaseViewModel
import ru.babaetskv.passionwoman.app.utils.applyDemoPresets
import ru.babaetskv.passionwoman.app.utils.notifier.Notifier
import ru.babaetskv.passionwoman.app.utils.toDemoPresets
import ru.babaetskv.passionwoman.domain.interactor.GetProfileUseCase
import ru.babaetskv.passionwoman.domain.interactor.UpdateProfileUseCase
import ru.babaetskv.passionwoman.domain.preferences.AppPreferences
import ru.babaetskv.passionwoman.domain.preferences.AuthPreferences
import ru.babaetskv.passionwoman.domain.utils.execute

class DemoPresetsViewModel(
    private val appPrefs: AppPreferences,
    private val authPrefs: AuthPreferences,
    private val getProfileUseCase: GetProfileUseCase,
    private val updateProfileUseCase: UpdateProfileUseCase,
    private val resources: Resources,
    notifier: Notifier,
    router: Router
) : BaseViewModel(notifier, router) {
    private val eventChannel = Channel<Event>(Channel.RENDEZVOUS)

    val presetsLiveData = MutableLiveData(appPrefs.toDemoPresets() + authPrefs.toDemoPresets())
    val eventBus: Flow<Event> = eventChannel.consumeAsFlow()

    fun onPresetChanged(preset: DemoPreset) {
        presetsLiveData.value?.find { it.id == preset.id }?.value = preset.value
    }

    fun onApplyPressed() {
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
            eventChannel.send(Event.LaunchMainFlow)
        }
    }

    sealed class Event {
        object LaunchMainFlow : Event()
    }
}
