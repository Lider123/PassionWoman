package ru.babaetskv.passionwoman.app.presentation.feature.profile

import android.util.Log
import android.viewbinding.library.fragment.viewBinding
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.babaetskv.passionwoman.app.R
import ru.babaetskv.passionwoman.app.databinding.FragmentProfileBinding
import ru.babaetskv.passionwoman.app.presentation.EmptyDividerDecoration
import ru.babaetskv.passionwoman.app.presentation.base.BaseFragment
import ru.babaetskv.passionwoman.app.utils.dialog.DIALOG_ACTIONS_ORIENTATION_HORIZONTAL
import ru.babaetskv.passionwoman.app.utils.dialog.DialogAction
import ru.babaetskv.passionwoman.app.utils.dialog.showAlertDialog
import ru.babaetskv.passionwoman.app.utils.load
import ru.babaetskv.passionwoman.app.utils.toFormattedPhone
import ru.babaetskv.passionwoman.domain.model.Profile

class ProfileFragment : BaseFragment<ProfileViewModel, BaseFragment.NoArgs>() {
    private val guestProfile: Profile
        get() = Profile(
            id = "-1",
            name = getString(R.string.profile_guest),
            surname = "",
            phone = "",
            avatar = null
        )
    private val binding: FragmentProfileBinding by viewBinding()
    private val profileMenuItemsAdapter: ProfileMenuItemAdapter by lazy {
        ProfileMenuItemAdapter(viewModel::onMenuItemPressed)
    }
    private var activeDialog: AlertDialog? = null

    override val layoutRes: Int = R.layout.fragment_profile
    override val viewModel: ProfileViewModel by viewModel()

    override fun initViews() {
        super.initViews()
        binding.run {
            rvMenuItems.run {
                adapter = profileMenuItemsAdapter
                addItemDecoration(EmptyDividerDecoration(requireContext(), R.dimen.margin_small))
            }
            btnLogOut.setOnClickListener {
                viewModel.onLogOutPressed()
            }
            layoutHeader.run {
                btnLogin.setOnClickListener {
                    viewModel.onLogInPressed()
                }
                btnEdit.setOnClickListener {
                    viewModel.onEditPressed()
                }
                ivAvatar.setOnClickListener {
                    viewModel.onAvatarPressed()
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

    private fun populateDialog(dialog: ProfileViewModel.Dialog?) {
        Log.e("TEST", "populateDialog(dialog=$dialog)") // TODO: remove
        dialog ?: run {
            activeDialog?.dismiss()
            activeDialog = null
            return
        }

        when (dialog) {
            ProfileViewModel.Dialog.LOG_OUT_CONFIRMATION -> showLogOutConfirmationDialog()
        }
    }

    private fun showLogOutConfirmationDialog() {
        Log.e("TEST", "showLogOutConfirmationDialog()") // TODO: remove
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
                btnEdit.isVisible = profile != guestProfile
            }
            btnLogOut.isVisible = profile != guestProfile
        }
    }

    companion object {

        fun create() = ProfileFragment()
    }
}
