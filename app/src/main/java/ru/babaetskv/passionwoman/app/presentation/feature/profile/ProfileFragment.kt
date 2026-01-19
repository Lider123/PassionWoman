package ru.babaetskv.passionwoman.app.presentation.feature.profile

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.compose.ui.platform.ComposeView
import com.github.dhaval2404.imagepicker.ImagePicker
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.babaetskv.passionwoman.app.R
import ru.babaetskv.passionwoman.app.analytics.constants.ScreenKeys
import ru.babaetskv.passionwoman.app.presentation.base.BaseFragment
import ru.babaetskv.passionwoman.app.presentation.base.FragmentComponent
import ru.babaetskv.passionwoman.app.presentation.event.Event
import ru.babaetskv.passionwoman.app.presentation.feature.profile.components.ProfileScreen
import ru.babaetskv.passionwoman.app.presentation.theme.PassionWomanTheme
import ru.babaetskv.passionwoman.app.utils.dialog.DIALOG_ACTIONS_ORIENTATION_HORIZONTAL
import ru.babaetskv.passionwoman.app.utils.dialog.DIALOG_ACTIONS_ORIENTATION_VERTICAL
import ru.babaetskv.passionwoman.app.utils.dialog.DialogAction
import ru.babaetskv.passionwoman.app.utils.dialog.showAlertDialog

class ProfileFragment : BaseFragment<ProfileViewModel, FragmentComponent.NoArgs>() {
    private var activeDialog: AlertDialog? = null

    override val layoutRes: Int = 0
    override val viewModel: ProfileViewModel by viewModel<ProfileViewModelImpl>()
    override val screenName: String = ScreenKeys.PROFILE

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = ComposeView(requireContext()).apply {
        setContent {
            PassionWomanTheme {
                ProfileScreen(viewModel)
            }
        }
    }

    @Deprecated("Deprecated in Java") // TODO: replace with a launcher
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

    override fun initObservers() {
        super.initObservers()
        viewModel.dialogLiveData.observe(viewLifecycleOwner, ::populateDialog)
    }

    override fun onEvent(event: Event) {
        when (event) {
            ProfileViewModel.PickCameraImageEvent -> {
                ImagePicker.with(this)
                    .cameraOnly()
                    .cropSquare()
                    .maxResultSize(400, 400)
                    .start()
            }
            ProfileViewModel.PickGalleryImageEvent -> {
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

    companion object {

        fun create() = ProfileFragment()
    }
}
