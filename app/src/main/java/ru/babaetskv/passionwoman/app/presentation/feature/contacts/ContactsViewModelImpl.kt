package ru.babaetskv.passionwoman.app.presentation.feature.contacts

import androidx.lifecycle.MutableLiveData
import ru.babaetskv.passionwoman.app.R
import ru.babaetskv.passionwoman.app.presentation.base.BaseViewModel
import ru.babaetskv.passionwoman.app.presentation.base.ViewModelDependencies
import ru.babaetskv.passionwoman.app.utils.externalaction.ExternalActionHandler

class ContactsViewModelImpl(
    private val externalActionHandler: ExternalActionHandler,
    dependencies: ViewModelDependencies
) : BaseViewModel(dependencies), ContactsViewModel {
    override val optionsLiveData = MutableLiveData(ContactsOption.values().asList())

    override fun onOptionPressed(option: ContactsOption) {
        when (option) {
            ContactsOption.PHONE -> {
                externalActionHandler.handleCall(R.string.phone)
            }
            ContactsOption.EMAIL -> {
                externalActionHandler.handleEmail(R.string.email)
            }
            ContactsOption.INSTAGRAM -> {
                externalActionHandler.handleOuterLink(R.string.instagram)
            }
            ContactsOption.TELEGRAM -> {
                externalActionHandler.handleOuterLink(R.string.telegram)
            }
        }
    }
}
