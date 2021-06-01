package ru.babaetskv.passionwoman.app.presentation.feature.auth.signup

import androidx.lifecycle.MutableLiveData
import com.github.terrakok.cicerone.Router
import ru.babaetskv.passionwoman.app.Screens
import ru.babaetskv.passionwoman.app.presentation.base.BaseViewModel
import ru.babaetskv.passionwoman.app.presentation.feature.profile.ProfileUpdatesListener
import ru.babaetskv.passionwoman.app.utils.notifier.Notifier
import ru.babaetskv.passionwoman.domain.interactor.UpdateProfileUseCase

class EditProfileViewModel(
    private val args: EditProfileFragment.Args,
    private val profileUpdatesListener: ProfileUpdatesListener,
    private val updateProfileUseCase: UpdateProfileUseCase,
    notifier: Notifier,
    router: Router
) : BaseViewModel(notifier, router) {
    private var name: String = args.profile.name
    private var surname: String = args.profile.surname
    private val dataIsValid: Boolean
        get() = name.isNotBlank() && surname.isNotBlank()

    val dataIsValidLiveData = MutableLiveData(dataIsValid)

    fun onNameChanged(name: String) {
        this.name = name
        dataIsValidLiveData.postValue(dataIsValid)
    }

    fun onSurnameChanged(surname: String) {
        this.surname = surname
        dataIsValidLiveData.postValue(dataIsValid)
    }

    fun onDonePressed() {
        if (!dataIsValid) return

        launchWithLoading {
            val newProfile = args.profile.copy(
                name = name,
                surname = surname
            )
            updateProfileUseCase.execute(newProfile)
            profileUpdatesListener.onProfileUpdated()
            if (args.signingUp) {
                router.newRootScreen(Screens.navigation())
            } else onBackPressed()
        }
    }
}
