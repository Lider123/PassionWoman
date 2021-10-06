package ru.babaetskv.passionwoman.app.presentation.feature.onboarding

import android.os.Build
import android.viewbinding.library.fragment.viewBinding
import androidx.core.view.isVisible
import androidx.viewpager2.widget.ViewPager2
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.babaetskv.passionwoman.app.R
import ru.babaetskv.passionwoman.app.Screens
import ru.babaetskv.passionwoman.app.databinding.FragmentOnboardingBinding
import ru.babaetskv.passionwoman.app.presentation.base.BaseFragment
import ru.babaetskv.passionwoman.app.presentation.base.FragmentComponent
import java.util.*
import kotlin.math.min

class OnboardingFragment : BaseFragment<OnboardingViewModel, OnboardingViewModel.Router, FragmentComponent.NoArgs>() {
    private val adapter: OnboardingPagesAdapter by lazy {
        val insets = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requireView().rootWindowInsets
        } else null
        OnboardingPagesAdapter(insets)
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

    override fun handleRouterEvent(event: OnboardingViewModel.Router) {
        super.handleRouterEvent(event)
        when (event) {
            OnboardingViewModel.Router.AuthScreen -> router.newRootScreen(Screens.auth())
        }
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
        binding.viewPager.offscreenPageLimit = min(pages.size, 3)
    }
    
    companion object {

        fun create() = OnboardingFragment()
    }
}
