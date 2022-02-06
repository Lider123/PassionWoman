package ru.babaetskv.passionwoman.app.presentation.feature.contacts

import androidx.lifecycle.MutableLiveData
import ru.babaetskv.passionwoman.app.R
import ru.babaetskv.passionwoman.app.presentation.base.BaseViewModel
import ru.babaetskv.passionwoman.app.presentation.event.RouterEvent
import ru.babaetskv.passionwoman.app.presentation.base.ViewModelDependencies
import ru.babaetskv.passionwoman.app.utils.ExternalIntentHandler

class ContactsViewModel(
    private val externalIntentHandler: ExternalIntentHandler,
    dependencies: ViewModelDependencies
) : BaseViewModel<ContactsViewModel.Router>(dependencies) {
    val optionsLiveData = MutableLiveData(ContactsOption.values().asList())

    fun onOptionPressed(option: ContactsOption) {
        when (option) {
            ContactsOption.PHONE -> externalIntentHandler.handleCall(R.string.phone)
            ContactsOption.EMAIL -> externalIntentHandler.handleEmail(R.string.email)
            ContactsOption.INSTAGRAM -> externalIntentHandler.handleOuterLink(R.string.instagram)
            ContactsOption.TELEGRAM -> externalIntentHandler.handleOuterLink(R.string.telegram)
        }
    }

    interface Router : RouterEvent
}
