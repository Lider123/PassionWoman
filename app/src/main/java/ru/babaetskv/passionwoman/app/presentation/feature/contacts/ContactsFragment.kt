package ru.babaetskv.passionwoman.app.presentation.feature.contacts

import android.viewbinding.library.fragment.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.babaetskv.passionwoman.app.BuildConfig
import ru.babaetskv.passionwoman.app.R
import ru.babaetskv.passionwoman.app.analytics.constants.ScreenKeys
import ru.babaetskv.passionwoman.app.databinding.FragmentContactsBinding
import ru.babaetskv.passionwoman.app.presentation.base.BaseBottomSheetDialogFragment
import ru.babaetskv.passionwoman.app.presentation.base.FragmentComponent

class ContactsFragment : BaseBottomSheetDialogFragment<ContactsViewModel, ContactsViewModel.Router, FragmentComponent.NoArgs>() {
    private val binding: FragmentContactsBinding by viewBinding()
    private val adapter: ContactsOptionsAdapter by lazy {
        ContactsOptionsAdapter(viewModel::onOptionPressed)
    }

    override val layoutRes: Int = R.layout.fragment_contacts
    override val viewModel: ContactsViewModel by viewModel<ContactsViewModelImpl>()
    override val screenName: String = ScreenKeys.CONTACTS

    override fun initViews() {
        super.initViews()
        binding.run {
            tvAppVersion.text = getString(R.string.contacts_version_placeholder,
                BuildConfig.VERSION_NAME
            )
            rvOptions.adapter = this@ContactsFragment.adapter
        }
    }

    override fun initObservers() {
        super.initObservers()
        viewModel.optionsLiveData.observe(this, ::updateOptions)
    }

    private fun updateOptions(options: List<ContactsOption>) {
        adapter.submitList(options)
    }

    companion object {

        fun create() = ContactsFragment()
    }
}
