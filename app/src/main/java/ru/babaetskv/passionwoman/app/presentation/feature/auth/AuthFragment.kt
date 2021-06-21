package ru.babaetskv.passionwoman.app.presentation.feature.auth

import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.EditorInfo
import android.viewbinding.library.fragment.viewBinding
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.collect
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.babaetskv.passionwoman.app.R
import ru.babaetskv.passionwoman.app.Screens
import ru.babaetskv.passionwoman.app.auth.AuthHandler
import ru.babaetskv.passionwoman.app.auth.AuthHandlerImpl
import ru.babaetskv.passionwoman.app.databinding.FragmentAuthBinding
import ru.babaetskv.passionwoman.app.presentation.base.BaseFragment
import ru.babaetskv.passionwoman.app.presentation.base.FragmentComponent
import ru.babaetskv.passionwoman.app.utils.hideAnimated
import ru.babaetskv.passionwoman.app.utils.hideKeyboard
import ru.babaetskv.passionwoman.app.utils.load
import ru.babaetskv.passionwoman.app.utils.showAnimated

class AuthFragment : BaseFragment<AuthViewModel, AuthViewModel.Router, FragmentComponent.NoArgs>() {
    private val binding: FragmentAuthBinding by viewBinding()
    private var smsAutoFilled = false
    private val authHandler: AuthHandler by lazy {
        AuthHandlerImpl(
            this,
            viewModel
        )
    }

    override val layoutRes: Int = R.layout.fragment_auth
    override val viewModel: AuthViewModel by viewModel()

    override fun initViews() {
        super.initViews()
        binding.run {
            ivBackground.load(R.drawable.bg_login)
            layoutLogin.run {
                countryCodePicker.run {
                    registerCarrierNumberEditText(etPhone)
                    setPhoneNumberValidityChangeListener { isValidNumber ->
                        btnLogin.isEnabled = isValidNumber
                    }
                }
                btnLogin.setOnClickListener {
                    hideKeyboard()
                    val phone = countryCodePicker.fullNumberWithPlus
                    val formattedPhone = countryCodePicker.formattedFullNumber
                    viewModel.onLoginPressed(phone, formattedPhone)
                }
                btnGuest.setOnClickListener {
                    hideKeyboard()
                    viewModel.onGuestPressed()
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
                btnBack.setOnClickListener {
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
        lifecycleScope.launchWhenResumed {
            viewModel.eventBus.collect(::handleEvent)
        }
    }

    override fun handleRouterEvent(event: AuthViewModel.Router) {
        super.handleRouterEvent(event)
        when (event) {
            AuthViewModel.Router.NavigationScreen -> router.newRootScreen(Screens.navigation())
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

    private fun populateMode(mode: AuthViewModel.Mode) {
        binding.run {
            when (mode) {
                AuthViewModel.Mode.LOGIN -> {
                    layoutSmsConfirm.root.hideAnimated(R.anim.fragment_fade_out)
                    layoutLogin.run {
                        root.showAnimated(R.anim.fragment_fade_in)
                        etPhone.requestFocus()
                    }
                }
                AuthViewModel.Mode.SMS_CONFIRM -> {
                    layoutLogin.root.hideAnimated(R.anim.fragment_fade_out)
                    layoutSmsConfirm.run {
                        root.showAnimated(R.anim.fragment_fade_in)
                        pevSmsCode.requestFocus()
                    }
                }
            }
        }
    }

    private fun populateLastPhone(phone: String) {
        binding.layoutSmsConfirm.tvTitle.text = getString(R.string.sms_title_template, phone)
    }

    private fun handleEvent(event: AuthViewModel.Event) {
        when (event) {
            is AuthViewModel.Event.LoginWithPhone -> {
                authHandler.loginWithPhone(event.phone, viewModel)
            }
        }
    }

    companion object {

        fun create() = AuthFragment()
    }
}
