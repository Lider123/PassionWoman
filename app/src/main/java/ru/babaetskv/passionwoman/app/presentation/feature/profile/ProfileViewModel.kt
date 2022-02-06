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
import ru.babaetskv.passionwoman.app.presentation.event.RouterEvent
import ru.babaetskv.passionwoman.app.presentation.base.ViewModelDependencies
import ru.babaetskv.passionwoman.domain.interactor.GetProfileUseCase
import ru.babaetskv.passionwoman.domain.interactor.LogOutUseCase
import ru.babaetskv.passionwoman.domain.interactor.UpdateAvatarUseCase
import ru.babaetskv.passionwoman.domain.model.Profile
import ru.babaetskv.passionwoman.domain.preferences.AuthPreferences
import ru.babaetskv.passionwoman.domain.utils.execute

class ProfileViewModel(
    private val getProfileUseCase: GetProfileUseCase,
    private val authPreferences: AuthPreferences,
    private val logOutUseCase: LogOutUseCase,
    private val updateAvatarUseCase: UpdateAvatarUseCase,
    dependencies: ViewModelDependencies
) : BaseViewModel<ProfileViewModel.Router>(dependencies), ProfileUpdatesListener {
    private val authTypeFlow = authPreferences.authTypeFlow.onEach(::onAuthTypeUpdated)
    private val eventChannel = Channel<Event>(Channel.RENDEZVOUS)

    val menuItemsLiveData = MutableLiveData(ProfileMenuItem.values().asList())
    val profileLiveData = MutableLiveData<Profile?>()
    val dialogLiveData = MutableLiveData<Dialog?>()
    val eventBus = eventChannel.receiveAsFlow()

    init {
        authTypeFlow.launchIn(this)
    }

    override fun onProfileUpdated() {
        loadProfile(false)
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

    fun onMenuItemPressed(item: ProfileMenuItem) {
        launch {
            when (item) {
                ProfileMenuItem.FAVORITES -> navigateTo(Router.FavoritesScreen)
                ProfileMenuItem.ORDERS -> {
                    // TODO
                    notifier.newRequest(this, R.string.in_development)
                        .sendAlert()
                }
                ProfileMenuItem.CONTACTS -> {
                    navigateTo(Router.ContactsScreen)
                }
            }
        }
    }

    fun onEditPressed() {
        val profile = profileLiveData.value ?: return

        launch {
            navigateTo(Router.EditProfileScreen(profile))
        }
    }

    fun onAvatarPressed() {
        profileLiveData.value ?: return

        dialogLiveData.postValue(Dialog.PICK_AVATAR)
    }

    fun onGalleryPressed() {
        dialogLiveData.postValue(null)
        launchWithLoading {
            eventChannel.send(Event.PickAvatarGallery)
        }
    }

    fun onCameraPressed() {
        dialogLiveData.postValue(null)
        launchWithLoading {
            eventChannel.send(Event.PickAvatarCamera)
        }
    }

    fun onLogOutPressed() {
        if (authPreferences.authType != AuthPreferences.AuthType.AUTHORIZED) return

        dialogLiveData.postValue(Dialog.LOG_OUT_CONFIRMATION)
    }

    fun onLogInPressed() {
        if (authPreferences.authType == AuthPreferences.AuthType.AUTHORIZED) return

        launch {
            navigateTo(Router.AuthScreen)
        }
    }

    fun onLogOutDeclined() {
        dialogLiveData.postValue(null)
    }

    fun onLogOutConfirmed() {
        launchWithLoading {
            logOutUseCase.execute()
            dialogLiveData.postValue(null)
        }
    }

    fun onImagePickSuccess(imageUri: Uri) {
        launchWithLoading {
            updateAvatarUseCase.execute(imageUri)
            loadProfile(false)
        }
    }

    fun onImagePickFailure() {
        notifier.newRequest(this, R.string.error_unknown)
            .sendAlert()
    }

    sealed class Event {
        object PickAvatarGallery : Event()
        object PickAvatarCamera : Event()
    }

    enum class Dialog {
        LOG_OUT_CONFIRMATION, PICK_AVATAR
    }

    sealed class Router : RouterEvent {

        object AuthScreen : Router()

        data class EditProfileScreen(
            val profile: Profile
        ) : Router()

        object FavoritesScreen : Router()

        object ContactsScreen: Router()
    }
}
