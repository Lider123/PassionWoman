package ru.babaetskv.passionwoman.app.presentation.feature.profile

import android.app.Activity
import android.content.Intent
import android.viewbinding.library.fragment.viewBinding
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import com.github.dhaval2404.imagepicker.ImagePicker
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.babaetskv.passionwoman.app.R
import ru.babaetskv.passionwoman.app.analytics.constants.ScreenKeys
import ru.babaetskv.passionwoman.app.navigation.Screens
import ru.babaetskv.passionwoman.app.databinding.FragmentProfileBinding
import ru.babaetskv.passionwoman.app.presentation.base.BaseFragment
import ru.babaetskv.passionwoman.app.presentation.base.FragmentComponent
import ru.babaetskv.passionwoman.app.presentation.event.InnerEvent
import ru.babaetskv.passionwoman.app.utils.dialog.DIALOG_ACTIONS_ORIENTATION_HORIZONTAL
import ru.babaetskv.passionwoman.app.utils.dialog.DIALOG_ACTIONS_ORIENTATION_VERTICAL
import ru.babaetskv.passionwoman.app.utils.dialog.DialogAction
import ru.babaetskv.passionwoman.app.utils.dialog.showAlertDialog
import ru.babaetskv.passionwoman.app.utils.load
import ru.babaetskv.passionwoman.app.utils.setOnSingleClickListener
import ru.babaetskv.passionwoman.app.utils.toFormattedPhone
import ru.babaetskv.passionwoman.domain.model.Profile

class ProfileFragment :
    BaseFragment<ProfileViewModel, ProfileViewModel.Router, FragmentComponent.NoArgs>() {
    private val binding: FragmentProfileBinding by viewBinding()
    private val profileMenuItemsAdapter: ProfileMenuItemAdapter by lazy {
        ProfileMenuItemAdapter(viewModel::onMenuItemPressed)
    }
    private var activeDialog: AlertDialog? = null

    override val layoutRes: Int = R.layout.fragment_profile
    override val viewModel: ProfileViewModel by viewModel<ProfileViewModelImpl>()
    override val screenName: String = ScreenKeys.PROFILE

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            ImagePicker.REQUEST_CODE -> {
                if (resultCode == Activity.RESULT_OK) {
                    val fileUri = data?.data!!
                    viewModel.onImagePickSuccess(fileUri)
                } else if (resultCode == ImagePicker.RESULT_ERROR) {
                    viewModel.onImagePickFailure()
                }
            }
            else -> super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun initViews() {
        super.initViews()
        binding.run {
            rvMenuItems.adapter = profileMenuItemsAdapter
            btnLogOut.setOnSingleClickListener {
                viewModel.onLogOutPressed()
            }
            layoutHeader.run {
                btnLogin.setOnSingleClickListener {
                    viewModel.onLogInPressed()
                }
                btnEdit.setOnSingleClickListener {
                    viewModel.onEditPressed()
                }
                btnEditAvatar.setOnSingleClickListener {
                    viewModel.onEditAvatarPressed()
                }
            }
        }
    }

    override fun initObservers() {
        super.initObservers()
        viewModel.profileLiveData.observe(viewLifecycleOwner, ::populateProfile)
        viewModel.menuItemsLiveData.observe(viewLifecycleOwner, ::populateMenu)
        viewModel.dialogLiveData.observe(viewLifecycleOwner, ::populateDialog)
    }

    override fun handleRouterEvent(event: ProfileViewModel.Router) {
        super.handleRouterEvent(event)
        when (event) {
            ProfileViewModel.Router.AuthScreen -> router.newRootScreen(Screens.auth())
            is ProfileViewModel.Router.EditProfileScreen -> {
                router.navigateTo(Screens.editProfile(event.profile))
            }
            ProfileViewModel.Router.FavoritesScreen -> router.navigateTo(Screens.favorites())
            ProfileViewModel.Router.OrdersScreen -> router.navigateTo(Screens.orders())
            ProfileViewModel.Router.ContactsScreen -> router.openBottomSheet(Screens.contacts())
        }
    }

    override fun onEvent(event: InnerEvent) {
        when (event) {
            InnerEvent.PickCameraImage -> {
                ImagePicker.with(this)
                    .cameraOnly()
                    .cropSquare()
                    .maxResultSize(400, 400)
                    .start()
            }
            InnerEvent.PickGalleryImage -> {
                ImagePicker.with(this)
                    .galleryOnly()
                    .cropSquare()
                    .maxResultSize(400, 400)
                    .start()
            }
            else -> super.onEvent(event)
        }
    }

    private fun populateDialog(dialog: ProfileViewModel.Dialog?) {
        dialog ?: run {
            activeDialog?.dismiss()
            activeDialog = null
            return
        }

        when (dialog) {
            ProfileViewModel.Dialog.LOG_OUT_CONFIRMATION -> showLogOutConfirmationDialog()
            ProfileViewModel.Dialog.PICK_AVATAR -> showPickAvatarDialog()
        }
    }

    private fun showPickAvatarDialog() {
        activeDialog = showAlertDialog(R.string.profile_pick_avatar_message,
            actionsOrientation = DIALOG_ACTIONS_ORIENTATION_VERTICAL,
            actions = listOf(
                DialogAction(getString(R.string.profile_camera),
                    isAccent = true
                ) {
                    viewModel.onCameraPressed()
                },
                DialogAction(getString(R.string.profile_gallery),
                    isAccent = true
                ) {
                    viewModel.onGalleryPressed()
                }
            )
        )
    }

    private fun showLogOutConfirmationDialog() {
        activeDialog = showAlertDialog(R.string.profile_log_out_confirmation_message,
            actionsOrientation = DIALOG_ACTIONS_ORIENTATION_HORIZONTAL,
            actions = listOf(
                DialogAction(getString(R.string.no),
                    isAccent = true
                ) {
                    viewModel.onLogOutDeclined()
                },
                DialogAction(getString(R.string.yes)) {
                    viewModel.onLogOutConfirmed()
                }
            )
        )
    }

    private fun populateMenu(items: List<ProfileMenuItem>) {
        profileMenuItemsAdapter.submitList(items)
    }

    private fun populateProfile(profile: Profile?) {
        val guestProfile = viewModel.guestProfile
        profile ?: run {
            populateProfile(guestProfile)
            return
        }

        binding.run {
            layoutHeader.run {
                profile.avatar?.let {
                    ivAvatar.load(it, R.drawable.avatar_placeholder)
                } ?: ivAvatar.setImageResource(R.drawable.avatar_placeholder)
                tvName.text = getString(R.string.profile_full_name_template, profile.name, profile.surname)
                tvPhone.text = profile.phone.toFormattedPhone()
                btnLogin.isVisible = profile == guestProfile
                btnEditAvatar.isVisible = profile != guestProfile
                btnEdit.isVisible = profile != guestProfile
            }
            btnLogOut.isVisible = profile != guestProfile
        }
    }

    companion object {

        fun create() = ProfileFragment()
    }
}
