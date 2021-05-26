package ru.babaetskv.passionwoman.app.presentation.feature.auth.signup

import android.os.Parcelable
import android.view.inputmethod.EditorInfo
import android.viewbinding.library.fragment.viewBinding
import androidx.core.widget.doAfterTextChanged
import kotlinx.parcelize.Parcelize
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import ru.babaetskv.passionwoman.app.R
import ru.babaetskv.passionwoman.app.databinding.FragmentSignUpBinding
import ru.babaetskv.passionwoman.app.presentation.base.BaseFragment
import ru.babaetskv.passionwoman.app.utils.hideKeyboard
import ru.babaetskv.passionwoman.domain.model.Profile

class SignUpFragment : BaseFragment<SignUpViewModel, SignUpFragment.Args>() {
    private val binding: FragmentSignUpBinding by viewBinding()

    override val layoutRes: Int = R.layout.fragment_sign_up
    override val viewModel: SignUpViewModel by viewModel { parametersOf(args) }

    override fun initViews() {
        super.initViews()
        binding.run {
            inputName.run {
                doAfterTextChanged {
                    viewModel.onNameChanged(it.toString())
                }
                requestFocus()
            }
            inputSurname.doAfterTextChanged {
                viewModel.onSurnameChanged(it.toString())
            }
            inputSurname.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    inputSurname.hideKeyboard()
                    btnDone.performClick()
                    return@setOnEditorActionListener true
                }
                return@setOnEditorActionListener false
            }
            btnDone.setOnClickListener {
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
        val profile: Profile
    ) : Parcelable

    companion object {

        fun create(profile: Profile) = SignUpFragment().withArgs(Args(profile))
    }
}