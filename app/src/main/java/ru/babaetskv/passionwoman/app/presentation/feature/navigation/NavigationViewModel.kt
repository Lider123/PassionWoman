package ru.babaetskv.passionwoman.app.presentation.feature.navigation

import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import ru.babaetskv.passionwoman.app.R
import ru.babaetskv.passionwoman.app.presentation.base.IViewModel
import ru.babaetskv.passionwoman.app.presentation.event.RouterEvent
import ru.babaetskv.passionwoman.app.presentation.feature.cart.CartFragment
import ru.babaetskv.passionwoman.app.presentation.feature.catalog.CatalogFragment
import ru.babaetskv.passionwoman.app.presentation.feature.home.HomeFragment
import ru.babaetskv.passionwoman.app.presentation.feature.profile.ProfileFragment
import ru.babaetskv.passionwoman.domain.model.Cart
import ru.babaetskv.passionwoman.domain.model.CartItem

interface NavigationViewModel : IViewModel {
    val selectedTabLiveData: LiveData<Tab>
    val dialogLiveData: LiveData<Dialog?>
    val cartLiveData: LiveData<Cart>

    fun onTabPressed(tab: Tab)

    enum class Tab(val tag: String, val menuItemId: Int, val fragmentFactory: () -> Fragment) {
        HOME("home", R.id.menu_home, HomeFragment::create),
        CATALOG("catalog", R.id.menu_catalog, CatalogFragment::create),
        CART("cart", R.id.menu_cart, CartFragment::create),
        PROFILE("profile", R.id.menu_profile, ProfileFragment::create);

        companion object {

            fun findByMenuItemId(menuItemId: Int) = values().find { it.menuItemId == menuItemId }
        }
    }

    sealed class Dialog {

        data class MergeFavorites(
            val callback: (merge: Boolean) -> Unit
        ) : Dialog()
    }

    sealed class Router : RouterEvent {

        data class AuthScreen(
            val onAppStart: Boolean
        ) : Router()

        data class ProductScreen(
            val productId: String
        ) : Router()
    }
}
