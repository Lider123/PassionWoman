package ru.babaetskv.passionwoman.app.presentation.feature.navigation

import android.app.Dialog
import android.viewbinding.library.fragment.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.babaetskv.passionwoman.app.R
import ru.babaetskv.passionwoman.app.Screens
import ru.babaetskv.passionwoman.app.databinding.FragmentNavigationBinding
import ru.babaetskv.passionwoman.app.presentation.base.BaseFragment
import ru.babaetskv.passionwoman.app.presentation.base.FragmentComponent
import ru.babaetskv.passionwoman.app.utils.dialog.DialogAction
import ru.babaetskv.passionwoman.app.utils.dialog.showAlertDialog

class NavigationFragment : BaseFragment<NavigationViewModel, NavigationViewModel.Router, FragmentComponent.NoArgs>() {
    private val binding: FragmentNavigationBinding by viewBinding()
    private var activeDialog: Dialog? = null

    override val layoutRes: Int = R.layout.fragment_navigation
    override val viewModel: NavigationViewModel by viewModel()
    override val applyTopInset: Boolean = false
    override val applyBottomInset: Boolean = false

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
        viewModel.dialogLiveData.observe(viewLifecycleOwner, ::populateDialog)
    }

    override fun handleRouterEvent(event: NavigationViewModel.Router) {
        super.handleRouterEvent(event)
        when (event) {
            NavigationViewModel.Router.AuthScreen -> router.newRootScreen(Screens.auth())
        }
    }

    private fun populateDialog(dialog: NavigationViewModel.Dialog?) {
        dialog ?: run {
            activeDialog?.dismiss()
            activeDialog = null
        }

        when (dialog) {
            is NavigationViewModel.Dialog.MergeFavorites -> showMergeFavoritesDialog(dialog)
        }
    }

    private fun showMergeFavoritesDialog(dialog: NavigationViewModel.Dialog.MergeFavorites) {
        activeDialog = showAlertDialog(R.string.navigation_merge_favorites_confirmation_message,
            actions = listOf(
                DialogAction(getString(R.string.yes),
                    isAccent = true
                ) {
                  dialog.callback.invoke(true)
                },
                DialogAction(getString(R.string.no)) {
                    dialog.callback.invoke(false)
                }
            )
        )
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
