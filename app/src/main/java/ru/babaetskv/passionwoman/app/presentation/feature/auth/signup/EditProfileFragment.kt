package ru.babaetskv.passionwoman.app.presentation.feature.auth.signup

import android.os.Parcelable
import android.view.inputmethod.EditorInfo
import android.viewbinding.library.fragment.viewBinding
import androidx.core.widget.doAfterTextChanged
import kotlinx.parcelize.Parcelize
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import ru.babaetskv.passionwoman.app.R
import ru.babaetskv.passionwoman.app.databinding.FragmentEditProfileBinding
import ru.babaetskv.passionwoman.app.presentation.base.BaseFragment
import ru.babaetskv.passionwoman.app.presentation.feature.profile.ProfileViewModel
import ru.babaetskv.passionwoman.app.utils.hideKeyboard
import ru.babaetskv.passionwoman.app.utils.load
import ru.babaetskv.passionwoman.domain.model.Profile

class EditProfileFragment : BaseFragment<EditProfileViewModel, EditProfileFragment.Args>() {
    private val binding: FragmentEditProfileBinding by viewBinding()
    private val profileViewModel: ProfileViewModel by sharedViewModel()

    override val layoutRes: Int = R.layout.fragment_edit_profile
    override val viewModel: EditProfileViewModel by viewModel {
        parametersOf(args, profileViewModel)
    }

    override fun initViews() {
        super.initViews()
        binding.run {
            toolbar.title = if (args.signingUp) {
                getString(R.string.edit_profile_sign_up)
            } else getString(R.string.edit_profile_edit)
            if (args.signingUp) ivBackground.load(R.drawable.bg_login)
            inputName.run {
                setText(args.profile.name)
                doAfterTextChanged {
                    viewModel.onNameChanged(it.toString())
                }
                requestFocus()
            }
            inputSurname.run {
                setText(args.profile.surname)
                doAfterTextChanged {
                    viewModel.onSurnameChanged(it.toString())
                }
            }
            inputSurname.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    hideKeyboard()
                    btnDone.performClick()
                    return@setOnEditorActionListener true
                }
                return@setOnEditorActionListener false
            }
            btnDone.setOnClickListener {
                hideKeyboard()
                viewModel.onDonePressed()
            }
            toolbar.setOnStartClickListener {
                viewModel.onBackPressed()
            }
        }
    }

    override fun initObservers() {
        super.initObservers()
        viewModel.dataIsValidLiveData.observe(viewLifecycleOwner, ::updateDoneButton)
    }

    private fun updateDoneButton(dataIsValid: Boolean) {
        binding.btnDone.isEnabled = dataIsValid
    }

    @Parcelize
    data class Args(
        val profile: Profile,
        val signingUp: Boolean
    ) : Parcelable

    companion object {

        fun create(profile: Profile, signingUp: Boolean) =
            EditProfileFragment().withArgs(Args(profile, signingUp))
    }
}
