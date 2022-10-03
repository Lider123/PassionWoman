package ru.babaetskv.passionwoman.app.presentation.feature.auth.signup

import androidx.lifecycle.LiveData
import ru.babaetskv.passionwoman.app.presentation.base.IViewModel

interface EditProfileViewModel : IViewModel {
    val dataIsValidLiveData: LiveData<Boolean>

    fun onNameChanged(name: String)
    fun onSurnameChanged(surname: String)
    fun onDonePressed()
}
