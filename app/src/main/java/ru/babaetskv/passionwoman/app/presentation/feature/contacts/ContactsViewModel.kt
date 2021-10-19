package ru.babaetskv.passionwoman.app.presentation.feature.contacts

import androidx.lifecycle.MutableLiveData
import ru.babaetskv.passionwoman.app.R
import ru.babaetskv.passionwoman.app.presentation.base.BaseViewModel
import ru.babaetskv.passionwoman.app.presentation.base.RouterEvent
import ru.babaetskv.passionwoman.app.utils.ExternalIntentHandler
import ru.babaetskv.passionwoman.app.utils.notifier.Notifier

class ContactsViewModel(
    private val externalIntentHandler: ExternalIntentHandler,
    notifier: Notifier
) : BaseViewModel<ContactsViewModel.Router>(notifier) {
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
