package ru.babaetskv.passionwoman.app.presentation.feature.profile

import android.net.Uri
import androidx.lifecycle.LiveData
import ru.babaetskv.passionwoman.app.presentation.base.IViewModel
import ru.babaetskv.passionwoman.app.presentation.event.RouterEvent
import ru.babaetskv.passionwoman.domain.model.Profile

interface ProfileViewModel : IViewModel {
    val menuItemsLiveData: LiveData<List<ProfileMenuItem>>
    val profileLiveData: LiveData<Profile?>
    val dialogLiveData: LiveData<Dialog?>
    val guestProfile: Profile

    fun onMenuItemPressed(item: ProfileMenuItem)
    fun onImagePickSuccess(imageUri: Uri)
    fun onImagePickFailure()
    fun onLogInPressed()
    fun onLogOutPressed()
    fun onEditPressed()
    fun onEditAvatarPressed()
    fun onCameraPressed()
    fun onGalleryPressed()
    fun onLogOutDeclined()
    fun onLogOutConfirmed()

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

        object OrdersScreen : Router()
    }
}
