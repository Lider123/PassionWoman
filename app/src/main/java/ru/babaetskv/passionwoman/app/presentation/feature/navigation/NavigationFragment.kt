package ru.babaetskv.passionwoman.app.presentation.feature.navigation

import android.app.Dialog
import android.os.Parcelable
import android.viewbinding.library.fragment.viewBinding
import com.google.android.material.navigation.NavigationBarView
import kotlinx.parcelize.Parcelize
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import ru.babaetskv.passionwoman.app.R
import ru.babaetskv.passionwoman.app.navigation.Screens
import ru.babaetskv.passionwoman.app.databinding.FragmentNavigationBinding
import ru.babaetskv.passionwoman.app.presentation.base.BaseFragment
import ru.babaetskv.passionwoman.app.utils.deeplink.DeeplinkPayload
import ru.babaetskv.passionwoman.app.utils.dialog.DialogAction
import ru.babaetskv.passionwoman.app.utils.dialog.showAlertDialog
import ru.babaetskv.passionwoman.domain.model.Cart

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

    override fun initViews() {
        super.initViews()
        (binding.navView as NavigationBarView).setOnItemSelectedListener { menuItem ->
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
        viewModel.cartLiveData.observe(viewLifecycleOwner, ::populateCart)
    }

    override fun handleRouterEvent(event: NavigationViewModel.Router) {
        super.handleRouterEvent(event)
        when (event) {
            is NavigationViewModel.Router.AuthScreen -> {
                // TODO: think about how to remove featured auth router events and use one general
                val screen = Screens.auth(event.onAppStart)
                if (event.onAppStart) {
                    router.newRootScreen(screen)
                } else router.navigateTo(screen)
            }
            is NavigationViewModel.Router.ProductScreen -> {
                router.navigateTo(Screens.productCard(event.productId))
            }
        }
    }

    private fun populateCart(cart: Cart) {
        val count = cart.items.size
        (binding.navView as NavigationBarView)
            .getOrCreateBadge(NavigationViewModel.Tab.CART.menuItemId)
            .run {
                if (count < 1) {
                    isVisible = false
                } else {
                    isVisible = true
                    number = count
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
        (binding.navView as NavigationBarView).menu.findItem(tab.menuItemId).isChecked = true
    }

    @Parcelize
    data class Args(
        val payload: DeeplinkPayload?
    ) : Parcelable

    companion object {

        fun create(payload: DeeplinkPayload?) = NavigationFragment().withArgs(Args(payload))
    }
}
