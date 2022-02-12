package ru.babaetskv.passionwoman.app.presentation.feature.auth.signup

import androidx.lifecycle.MutableLiveData
import ru.babaetskv.passionwoman.app.analytics.event.SignUpEvent
import ru.babaetskv.passionwoman.app.presentation.base.BaseViewModel
import ru.babaetskv.passionwoman.app.presentation.base.ViewModelDependencies
import ru.babaetskv.passionwoman.app.presentation.event.InnerEvent
import ru.babaetskv.passionwoman.domain.interactor.UpdateProfileUseCase

class EditProfileViewModelImpl(
    private val args: EditProfileFragment.Args,
    private val updateProfileUseCase: UpdateProfileUseCase,
    dependencies: ViewModelDependencies
) : BaseViewModel<EditProfileViewModel.Router>(dependencies), EditProfileViewModel {
    private var name: String = args.profile.name
    private var surname: String = args.profile.surname
    private val dataIsValid: Boolean
        get() = name.isNotBlank() && surname.isNotBlank()

    override val dataIsValidLiveData = MutableLiveData(dataIsValid)

    override fun onNameChanged(name: String) {
        this.name = name
        dataIsValidLiveData.postValue(dataIsValid)
    }

    override fun onSurnameChanged(surname: String) {
        this.surname = surname
        dataIsValidLiveData.postValue(dataIsValid)
    }

    override fun onDonePressed() {
        if (!dataIsValid) return

        launchWithLoading {
            val newProfile = args.profile.copy(
                name = name,
                surname = surname
            )
            updateProfileUseCase.execute(newProfile)
            sendEvent(InnerEvent.UpdateProfile)
            if (args.signingUp) {
                analyticsHandler.log(SignUpEvent())
                navigateTo(EditProfileViewModel.Router.NavigationScreen)
            } else onBackPressed()
        }
    }
}
