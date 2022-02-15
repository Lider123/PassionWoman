package ru.babaetskv.passionwoman.app.presentation.feature.navigation

import android.app.Dialog
import android.os.Parcelable
import android.viewbinding.library.fragment.viewBinding
import kotlinx.parcelize.Parcelize
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import ru.babaetskv.passionwoman.app.R
import ru.babaetskv.passionwoman.app.navigation.Screens
import ru.babaetskv.passionwoman.app.databinding.FragmentNavigationBinding
import ru.babaetskv.passionwoman.app.presentation.base.BaseFragment
import ru.babaetskv.passionwoman.app.presentation.view.highlight.Highlight
import ru.babaetskv.passionwoman.app.presentation.view.highlight.shape.CircleShape
import ru.babaetskv.passionwoman.app.presentation.view.highlight.target.ViewTarget
import ru.babaetskv.passionwoman.app.utils.color
import ru.babaetskv.passionwoman.app.utils.deeplink.DeeplinkPayload
import ru.babaetskv.passionwoman.app.utils.dialog.DialogAction
import ru.babaetskv.passionwoman.app.utils.dialog.showAlertDialog
import ru.babaetskv.passionwoman.app.utils.dip
import ru.babaetskv.passionwoman.app.utils.getMenuItemView

class NavigationFragment : BaseFragment<NavigationViewModel, NavigationViewModel.Router, NavigationFragment.Args>() {
    private val binding: FragmentNavigationBinding by viewBinding()
    private var activeDialog: Dialog? = null

    override val layoutRes: Int = R.layout.fragment_navigation
    override val viewModel: NavigationViewModel by viewModel<NavigationViewModelImpl> {
        parametersOf(args)
    }
    override val applyTopInset: Boolean = false
    override val applyBottomInset: Boolean = false
    override val screenName: String = ""

    override fun onStart() {
        super.onStart()
        Highlight.Builder(requireContext())
            .setShape(CircleShape())
            .setFrameMargin(requireContext().dip(-8))
            .setOutlineColor(requireContext().color(R.color.secondary))
            .build()
            .run {
                prepare(ViewTarget(binding.navView.getMenuItemView(0)), requireActivity())
                showOnReady = true
            }
    }

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
            is NavigationViewModel.Router.ProductScreen -> {
                router.navigateTo(Screens.productCard(event.productId))
            }
        }
    }

    private fun populateDialog(dialog: NavigationViewModel.Dialog?) {
        dialog ?: run {
            activeDialog?.dismiss()
            activeDialog = null
        }

        when (dialog) {
            is NavigationViewModel.Dialog.MergeFavorites -> showMergeFavoritesDialog(dialog)
            else -> Unit
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

    @Parcelize
    data class Args(
        val payload: DeeplinkPayload?
    ) : Parcelable

    companion object {

        fun create(payload: DeeplinkPayload?) = NavigationFragment().withArgs(Args(payload))
    }
}
