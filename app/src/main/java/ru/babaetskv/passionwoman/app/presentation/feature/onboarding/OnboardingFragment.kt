package ru.babaetskv.passionwoman.app.presentation.feature.onboarding

import android.os.Build
import android.os.Bundle
import android.viewbinding.library.fragment.viewBinding
import androidx.core.view.isVisible
import androidx.viewpager2.widget.ViewPager2
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.babaetskv.passionwoman.app.R
import ru.babaetskv.passionwoman.app.analytics.constants.ScreenKeys
import ru.babaetskv.passionwoman.app.databinding.FragmentOnboardingBinding
import ru.babaetskv.passionwoman.app.presentation.base.BaseFragment
import ru.babaetskv.passionwoman.app.presentation.base.FragmentComponent
import java.util.*
import kotlin.math.min

class OnboardingFragment : BaseFragment<OnboardingViewModel, FragmentComponent.NoArgs>() {
    private val adapter: OnboardingPagesAdapter by lazy {
        OnboardingPagesAdapter()
    }
    private val binding: FragmentOnboardingBinding by viewBinding()
    private var smoothScroll = false

    override val layoutRes: Int = R.layout.fragment_onboarding
    override val viewModel: OnboardingViewModel by viewModel<OnboardingViewModelImpl>()
    override val screenName: String = ScreenKeys.ONBOARDING

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        smoothScroll = false
    }

    override fun initViews() {
        super.initViews()
        val insets = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requireView().rootWindowInsets
        } else null
        adapter.setWindowInsets(insets)
        // TODO: fix bottom inset when screen is rotated twice

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
            viewPager.setCurrentItem(page, smoothScroll)
            smoothScroll = true
            btnPrev.isVisible = page > 0
            btnNext.isVisible = page < adapter.itemCount - 1
        }
    }

    private fun populatePages(pages: List<OnboardingPage>) {
        adapter.submitList(pages)
        binding.viewPager.offscreenPageLimit = min(pages.size, 3)
    }
    
    companion object {

        fun create() = OnboardingFragment()
    }
}
