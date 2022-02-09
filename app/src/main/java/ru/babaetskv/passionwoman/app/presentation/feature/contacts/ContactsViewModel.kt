package ru.babaetskv.passionwoman.app.presentation.feature.contacts

import androidx.lifecycle.LiveData
import ru.babaetskv.passionwoman.app.presentation.base.IViewModel
import ru.babaetskv.passionwoman.app.presentation.event.RouterEvent

interface ContactsViewModel : IViewModel {
    val optionsLiveData: LiveData<List<ContactsOption>>

    fun onOptionPressed(option: ContactsOption)

    interface Router : RouterEvent
}
