package ru.babaetskv.passionwoman.app.presentation.feature.onboarding

import android.viewbinding.library.fragment.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.babaetskv.passionwoman.app.R
import ru.babaetskv.passionwoman.app.databinding.FragmentOnboardingBinding
import ru.babaetskv.passionwoman.app.presentation.base.BaseFragment

class OnboardingFragment : BaseFragment<OnboardingViewModel, BaseFragment.NoArgs>() {
    private val adapter: OnboardingPagesAdapter by lazy {
        OnboardingPagesAdapter()
    }
    private val binding: FragmentOnboardingBinding by viewBinding()

    override val layoutRes: Int = R.layout.fragment_onboarding
    override val viewModel: OnboardingViewModel by viewModel()

    override fun initViews() {
        super.initViews()
        binding.run {
            viewPager.adapter = adapter
            pageIndicator.setViewPager(viewPager)
            adapter.registerAdapterDataObserver(pageIndicator.adapterDataObserver)

        }
    }

    override fun initObservers() {
        super.initObservers()
        viewModel.pagesLiveData.observe(viewLifecycleOwner, ::populatePages)
    }

    private fun populatePages(pages: List<OnboardingPage>) {
        adapter.submitList(pages)
    }
    
    companion object {

        fun create() = OnboardingFragment()
    }
}
