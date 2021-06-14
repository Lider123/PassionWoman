package ru.babaetskv.passionwoman.app.presentation.feature.navigation

import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import com.github.terrakok.cicerone.Router
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.babaetskv.passionwoman.app.R
import ru.babaetskv.passionwoman.app.Screens
import ru.babaetskv.passionwoman.app.presentation.base.BaseViewModel
import ru.babaetskv.passionwoman.app.presentation.feature.InDevelopmentFragment
import ru.babaetskv.passionwoman.app.presentation.feature.catalog.CatalogFragment
import ru.babaetskv.passionwoman.app.presentation.feature.home.HomeFragment
import ru.babaetskv.passionwoman.app.presentation.feature.profile.ProfileFragment
import ru.babaetskv.passionwoman.app.utils.notifier.Notifier
import ru.babaetskv.passionwoman.domain.preferences.AuthPreferences

class NavigationViewModel(
    authPreferences: AuthPreferences,
    notifier: Notifier,
    router: Router
) : BaseViewModel(notifier, router) {
    private val authTypeFlow = authPreferences.authTypeFlow.onEach(::onAuthTypeUpdated)

    val selectedTabLiveData = MutableLiveData(Tab.HOME)

    init {
        authTypeFlow.launchIn(this)
    }

    private suspend fun onAuthTypeUpdated(authType: AuthPreferences.AuthType) {
        when (authType) {
            AuthPreferences.AuthType.NONE -> router.newRootScreen(Screens.auth())
            else -> Unit
        }
    }

    fun onTabPressed(tab: Tab) {
        selectedTabLiveData.postValue(tab)
    }

    enum class Tab(val tag: String, val menuItemId: Int, val fragmentFactory: () -> Fragment) {
        HOME("home", R.id.menu_home, {
            HomeFragment.create()
        }),
        CATALOG("catalog", R.id.menu_catalog, {
            CatalogFragment.create()
        }),
        CART("cart", R.id.menu_cart, {
            InDevelopmentFragment.create() // TODO : set up cart fragment
        }),
        PROFILE("profile", R.id.menu_profile, {
            ProfileFragment.create()
        });

        companion object {

            fun findByMenuItemId(menuItemId: Int) = values().find { it.menuItemId == menuItemId }
        }
    }
}
