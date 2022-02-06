package ru.babaetskv.passionwoman.app.presentation.feature.profile

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import ru.babaetskv.passionwoman.app.R
import ru.babaetskv.passionwoman.app.presentation.base.BaseViewModel
import ru.babaetskv.passionwoman.app.presentation.base.ViewModelDependencies
import ru.babaetskv.passionwoman.domain.interactor.GetProfileUseCase
import ru.babaetskv.passionwoman.domain.interactor.LogOutUseCase
import ru.babaetskv.passionwoman.domain.interactor.UpdateAvatarUseCase
import ru.babaetskv.passionwoman.domain.interactor.exception.StringProvider
import ru.babaetskv.passionwoman.domain.model.Profile
import ru.babaetskv.passionwoman.domain.preferences.AuthPreferences
import ru.babaetskv.passionwoman.domain.utils.execute

class ProfileViewModelImpl(
    private val getProfileUseCase: GetProfileUseCase,
    private val authPreferences: AuthPreferences,
    private val logOutUseCase: LogOutUseCase,
    private val updateAvatarUseCase: UpdateAvatarUseCase,
    private val stringProvider: StringProvider,
    dependencies: ViewModelDependencies
) : BaseViewModel<ProfileViewModel.Router>(dependencies), ProfileViewModel, ProfileUpdatesListener {
    private val authTypeFlow = authPreferences.authTypeFlow.onEach(::onAuthTypeUpdated)
    private val eventChannel = Channel<ProfileViewModel.Event>(Channel.RENDEZVOUS)

    override val menuItemsLiveData = MutableLiveData(ProfileMenuItem.values().asList())
    override val profileLiveData = MutableLiveData<Profile?>()
    override val dialogLiveData = MutableLiveData<ProfileViewModel.Dialog?>()
    override val eventBus = eventChannel.receiveAsFlow()

    override val guestProfile: Profile
        get() = Profile(
            id = "-1",
            name = stringProvider.GUEST_PROFILE_NAME,
            surname = "",
            phone = "",
            avatar = null
        )

    init {
        authTypeFlow.launchIn(this)
    }

    override fun onProfileUpdated() {
        loadProfile(false)
    }

    override fun onMenuItemPressed(item: ProfileMenuItem) {
        launch {
            when (item) {
                ProfileMenuItem.FAVORITES -> {
                    navigateTo(ProfileViewModel.Router.FavoritesScreen)
                }
                ProfileMenuItem.ORDERS -> {
                    // TODO
                    notifier.newRequest(this, R.string.in_development)
                        .sendAlert()
                }
                ProfileMenuItem.CONTACTS -> {
                    navigateTo(ProfileViewModel.Router.ContactsScreen)
                }
            }
        }
    }

    override fun onEditPressed() {
        val profile = profileLiveData.value ?: return

        launch {
            navigateTo(ProfileViewModel.Router.EditProfileScreen(profile))
        }
    }

    override fun onAvatarPressed() {
        profileLiveData.value ?: return

        dialogLiveData.postValue(ProfileViewModel.Dialog.PICK_AVATAR)
    }

    override fun onGalleryPressed() {
        dialogLiveData.postValue(null)
        launchWithLoading {
            eventChannel.send(ProfileViewModel.Event.PickAvatarGallery)
        }
    }

    override fun onCameraPressed() {
        dialogLiveData.postValue(null)
        launchWithLoading {
            eventChannel.send(ProfileViewModel.Event.PickAvatarCamera)
        }
    }

    override fun onLogOutPressed() {
        if (authPreferences.authType != AuthPreferences.AuthType.AUTHORIZED) return

        dialogLiveData.postValue(ProfileViewModel.Dialog.LOG_OUT_CONFIRMATION)
    }

    override fun onLogInPressed() {
        if (authPreferences.authType == AuthPreferences.AuthType.AUTHORIZED) return

        launch {
            navigateTo(ProfileViewModel.Router.AuthScreen)
        }
    }

    override fun onLogOutDeclined() {
        dialogLiveData.postValue(null)
    }

    override fun onLogOutConfirmed() {
        launchWithLoading {
            logOutUseCase.execute()
            dialogLiveData.postValue(null)
        }
    }

    override fun onImagePickSuccess(imageUri: Uri) {
        launchWithLoading {
            updateAvatarUseCase.execute(imageUri)
            loadProfile(false)
        }
    }

    override fun onImagePickFailure() {
        notifier.newRequest(this, R.string.error_unknown)
            .sendAlert()
    }

    private fun onAuthTypeUpdated(authType: AuthPreferences.AuthType) {
        when (authType) {
            AuthPreferences.AuthType.AUTHORIZED -> loadProfile(false)
            AuthPreferences.AuthType.GUEST -> loadProfile(true)
            else -> Unit
        }
    }

    private fun loadProfile(isGuest: Boolean) {
        if (isGuest) {
            profileLiveData.postValue(null)
        } else {
            launchWithLoading {
                profileLiveData.postValue(getProfileUseCase.execute())
            }
        }
    }
}
