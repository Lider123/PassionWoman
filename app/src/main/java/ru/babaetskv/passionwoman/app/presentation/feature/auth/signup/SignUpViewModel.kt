package ru.babaetskv.passionwoman.app.presentation.feature.auth.signup

import androidx.lifecycle.MutableLiveData
import com.github.terrakok.cicerone.Router
import ru.babaetskv.passionwoman.app.Screens
import ru.babaetskv.passionwoman.app.presentation.base.BaseViewModel
import ru.babaetskv.passionwoman.app.utils.notifier.Notifier
import ru.babaetskv.passionwoman.domain.interactor.UpdateProfileUseCase

class SignUpViewModel(
    private val args: SignUpFragment.Args,
    private val updateProfileUseCase: UpdateProfileUseCase,
    notifier: Notifier,
    router: Router
) : BaseViewModel(notifier, router) {
    private var name: String = ""
    private var surname: String = ""
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
            router.newRootScreen(Screens.navigation())
        }
    }
}
