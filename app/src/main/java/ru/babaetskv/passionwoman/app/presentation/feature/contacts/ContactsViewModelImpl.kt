package ru.babaetskv.passionwoman.app.presentation.feature.contacts

import androidx.lifecycle.MutableLiveData
import ru.babaetskv.passionwoman.app.R
import ru.babaetskv.passionwoman.app.presentation.base.BaseViewModel
import ru.babaetskv.passionwoman.app.presentation.base.ViewModelDependencies
import ru.babaetskv.passionwoman.app.utils.ExternalIntentHandler

class ContactsViewModelImpl(
    private val externalIntentHandler: ExternalIntentHandler,
    dependencies: ViewModelDependencies
) : BaseViewModel<ContactsViewModel.Router>(dependencies), ContactsViewModel {
    override val optionsLiveData =
        MutableLiveData(ContactsOption.values().asList())

    override fun onOptionPressed(option: ContactsOption) {
        when (option) {
            ContactsOption.PHONE -> {
                externalIntentHandler.handleCall(R.string.phone)
            }
            ContactsOption.EMAIL -> {
                externalIntentHandler.handleEmail(R.string.email)
            }
            ContactsOption.INSTAGRAM -> {
                externalIntentHandler.handleOuterLink(R.string.instagram)
            }
            ContactsOption.TELEGRAM -> {
                externalIntentHandler.handleOuterLink(R.string.telegram)
            }
        }
    }
}
