package ru.babaetskv.passionwoman.app.presentation.feature.navigation

import android.viewbinding.library.fragment.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.babaetskv.passionwoman.app.R
import ru.babaetskv.passionwoman.app.Screens
import ru.babaetskv.passionwoman.app.databinding.FragmentNavigationBinding
import ru.babaetskv.passionwoman.app.presentation.base.BaseFragment

class NavigationFragment : BaseFragment<NavigationViewModel, NavigationViewModel.Router, BaseFragment.NoArgs>() {
    private val binding: FragmentNavigationBinding by viewBinding()

    override val layoutRes: Int = R.layout.fragment_navigation
    override val viewModel: NavigationViewModel by viewModel()

    override fun initViews() {
        super.initViews()
        binding.navView.setOnNavigationItemSelectedListener { menuItem ->
            NavigationViewModel.Tab.findByMenuItemId(menuItem.itemId)?.let {
                viewModel.onTabPressed(it)
                true
            } ?: false
        }

    }

    override fun initObservers() {
        super.initObservers()
        viewModel.selectedTabLiveData.observe(viewLifecycleOwner, ::showTab)
    }

    override fun handleRouterEvent(event: NavigationViewModel.Router) {
        super.handleRouterEvent(event)
        when (event) {
            NavigationViewModel.Router.AuthScreen -> router.newRootScreen(Screens.auth())
        }
    }

    private fun showTab(tab: NavigationViewModel.Tab) {
        val tag = tab.tag
        val currentFragment = childFragmentManager.fragments.find { it.isVisible }
        val nextFragment = childFragmentManager.findFragmentByTag(tag)
        if (currentFragment != null && nextFragment != null && currentFragment === nextFragment) return

        childFragmentManager.beginTransaction().apply {
            setCustomAnimations(R.anim.fragment_fade_in, R.anim.fragment_fade_out, R.anim.fragment_fade_in, R.anim.fragment_fade_out)
            childFragmentManager.fragments.forEach { detach(it) }
            if (nextFragment == null) {
                add(R.id.container, tab.fragmentFactory.invoke(), tag)
            } else {
                attach(nextFragment)
            }
        }.commitNow()
        binding.navView.menu.findItem(tab.menuItemId).isChecked = true
    }

    companion object {

        fun create() = NavigationFragment()
    }
}
