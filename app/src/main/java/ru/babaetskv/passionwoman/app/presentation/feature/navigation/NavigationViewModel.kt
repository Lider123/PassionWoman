package ru.babaetskv.passionwoman.app.presentation.feature.navigation

import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import com.github.terrakok.cicerone.Router
import ru.babaetskv.passionwoman.app.R
import ru.babaetskv.passionwoman.app.presentation.base.BaseViewModel
import ru.babaetskv.passionwoman.app.presentation.feature.InDevelopmentFragment
import ru.babaetskv.passionwoman.app.presentation.feature.catalog.CatalogFragment
import ru.babaetskv.passionwoman.app.utils.notifier.Notifier

class NavigationViewModel(notifier: Notifier, router: Router) : BaseViewModel(notifier, router) {
    val selectedTabLiveData = MutableLiveData(Tab.HOME)

    fun onTabPressed(tab: Tab) {
        selectedTabLiveData.postValue(tab)
    }

    enum class Tab(val tag: String, val menuItemId: Int, val fragmentFactory: () -> Fragment) {
        HOME("home", R.id.menu_home, {
            CatalogFragment.create() // TODO: set up home fragment
        }),
        FAVORITES("favorites", R.id.menu_favorites, {
            InDevelopmentFragment.create() // TODO : set up favorites fragment
        }),
        CART("cart", R.id.menu_cart, {
            InDevelopmentFragment.create() // TODO : set up cart fragment
        }),
        PROFILE("profile", R.id.menu_profile, {
            InDevelopmentFragment.create() // TODO : set up profile fragment
        });

        companion object {

            fun findByMenuItemId(menuItemId: Int) = values().find { it.menuItemId == menuItemId }
        }
    }
}
