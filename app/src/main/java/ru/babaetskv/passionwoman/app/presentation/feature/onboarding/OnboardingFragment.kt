package ru.babaetskv.passionwoman.app.presentation.feature.onboarding

import android.viewbinding.library.fragment.viewBinding
import androidx.core.view.isVisible
import androidx.viewpager2.widget.ViewPager2
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
            viewPager.run {
                adapter = this@OnboardingFragment.adapter.apply {
                    registerAdapterDataObserver(pageIndicator.adapterDataObserver)
                }
                pageIndicator.setViewPager(this)
                registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {

                    override fun onPageSelected(position: Int) {
                        super.onPageSelected(position)
                        viewModel.onCurrPageChanged(position)
                    }
                })
            }
            btnPrev.setOnClickListener {
                viewModel.onPrevPagePressed()
            }
            btnNext.setOnClickListener {
                viewModel.onNextPagePressed()
            }
        }
    }

    override fun initObservers() {
        super.initObservers()
        viewModel.pagesLiveData.observe(viewLifecycleOwner, ::populatePages)
        viewModel.currPageLiveData.observe(viewLifecycleOwner, ::populateCurrPage)
    }

    private fun populateCurrPage(page: Int) {
        binding.run {
            viewPager.currentItem = page
            btnPrev.isVisible = page > 0
            btnNext.isVisible = page < adapter.itemCount - 1
        }
    }

    private fun populatePages(pages: List<OnboardingPage>) {
        adapter.submitList(pages)
    }
    
    companion object {

        fun create() = OnboardingFragment()
    }
}