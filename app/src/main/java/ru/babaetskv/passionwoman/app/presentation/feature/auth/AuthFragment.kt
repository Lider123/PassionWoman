package ru.babaetskv.passionwoman.app.presentation.feature.auth

import android.content.Intent
import android.os.Parcelable
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.EditorInfo
import android.viewbinding.library.fragment.viewBinding
import androidx.core.view.isVisible
import kotlinx.parcelize.Parcelize
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import ru.babaetskv.passionwoman.app.R
import ru.babaetskv.passionwoman.app.analytics.constants.ScreenKeys
import ru.babaetskv.passionwoman.app.auth.AuthHandler
import ru.babaetskv.passionwoman.app.auth.AuthHandlerImpl
import ru.babaetskv.passionwoman.app.databinding.FragmentAuthBinding
import ru.babaetskv.passionwoman.app.presentation.base.BaseFragment
import ru.babaetskv.passionwoman.app.presentation.event.Event
import ru.babaetskv.passionwoman.app.utils.*

class AuthFragment : BaseFragment<AuthViewModel, AuthFragment.Args>() {
    private val binding: FragmentAuthBinding by viewBinding()
    private var smsAutoFilled = false
    private val authHandler: AuthHandler by lazy {
        AuthHandlerImpl(
            this,
            viewModel
        )
    }

    override val layoutRes: Int = R.layout.fragment_auth
    override val viewModel: AuthViewModel by viewModel<AuthViewModelImpl> {
        parametersOf(args)
    }
    override val screenName: String = ScreenKeys.LOGIN

    override fun initViews() {
        super.initViews()
        binding.run {
            layoutLogin.run {
                countryCodePicker.run {
                    registerCarrierNumberEditText(etPhone)
                    setPhoneNumberValidityChangeListener { isValidNumber ->
                        btnLogin.isEnabled = isValidNumber
                    }
                }
                btnLogin.setOnSingleClickListener {
                    hideKeyboard()
                    val phone = countryCodePicker.fullNumberWithPlus
                    val formattedPhone = countryCodePicker.formattedFullNumber
                    viewModel.onLoginPressed(phone, formattedPhone)
                }
                groupGuest.isVisible = args.onAppStart
                btnGuest.setOnSingleClickListener {
                    hideKeyboard()
                    viewModel.onGuestPressed()
                }
                btnBack.run {
                    isVisible = !args.onAppStart
                    setOnSingleClickListener {
                        viewModel.onBackPressed()
                    }
                }
                etPhone.run {
                    setOnEditorActionListener { _, actionId, _ ->
                        if (actionId == EditorInfo.IME_ACTION_DONE) {
                            btnLogin.performClick()
                            true
                        } else false
                    }
                    requestFocus()
                }
            }
            layoutSmsConfirm.run {
                btnBack.setOnSingleClickListener {
                    viewModel.onBackPressed()
                }
                pevSmsCode.addTextChangedListener(object : TextWatcher {

                    override fun beforeTextChanged(
                        s: CharSequence?,
                        start: Int,
                        count: Int,
                        after: Int
                    ) = Unit

                    override fun onTextChanged(
                        s: CharSequence?,
                        start: Int,
                        before: Int,
                        count: Int
                    ) = Unit

                    override fun afterTextChanged(s: Editable?) {
                        val code = s.toString()
                        if (smsAutoFilled) {
                            smsAutoFilled = false
                        } else if (code.length == AuthHandler.SMS_CODE_LENGTH) {
                            pevSmsCode.hideKeyboard()
                            verifySms(code)
                        }
                    }
                })
            }
        }
    }

    override fun initObservers() {
        super.initObservers()
        viewModel.lastPhoneLiveData.observe(viewLifecycleOwner, ::populateLastPhone)
        viewModel.modeLiveData.observe(viewLifecycleOwner, ::populateMode)
        viewModel.smsCodeLiveData.observe(viewLifecycleOwner, ::populateSmsCode)
    }

    override fun onEvent(event: Event) {
        when (event) {
            is AuthViewModel.LoginWithPhoneEvent -> {
                authHandler.loginWithPhone(event.phone, viewModel)
            }
            else -> super.onEvent(event)
        }
    }

    private fun populateSmsCode(code: String) {
        smsAutoFilled = true
        binding.layoutSmsConfirm.pevSmsCode.setText(code)
    }

    private fun verifySms(code: String) {
        Intent().apply {
            putExtra(AuthHandler.EXTRA_SMS_CODE, code)
        }.let {
            authHandler.onActivityResult(AuthHandler.REQUEST_PHONE_SIGN_IN, 0, it)
        }
    }

    private fun populateMode(mode: AuthViewModel.AuthMode) {
        binding.run {
            when (mode) {
                AuthViewModel.AuthMode.LOGIN -> {
                    layoutSmsConfirm.root.hideAnimated(R.anim.fragment_fade_out)
                    layoutLogin.run {
                        root.showAnimated(R.anim.fragment_fade_in) {
                            etPhone.requestFocus()
                        }
                    }
                }
                AuthViewModel.AuthMode.SMS_CONFIRM -> {
                    layoutLogin.root.hideAnimated(R.anim.fragment_fade_out)
                    layoutSmsConfirm.run {
                        root.showAnimated(R.anim.fragment_fade_in) {
                            pevSmsCode.requestFocus()
                        }
                    }
                }
            }
            viewModel.onModeChanged(mode)
        }
    }

    private fun populateLastPhone(phone: String) {
        binding.layoutSmsConfirm.tvTitle.text = getString(R.string.sms_title_template, phone)
    }

    @Parcelize
    data class Args(
        val onAppStart: Boolean
    ) : Parcelable

    companion object {

        fun create(onAppStart: Boolean) = AuthFragment().withArgs(Args(onAppStart))
    }
}
