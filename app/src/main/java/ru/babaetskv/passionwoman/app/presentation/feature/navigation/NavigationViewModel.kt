package ru.babaetskv.passionwoman.app.presentation.feature.navigation

import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.babaetskv.passionwoman.app.R
import ru.babaetskv.passionwoman.app.presentation.base.BaseViewModel
import ru.babaetskv.passionwoman.app.presentation.base.RouterEvent
import ru.babaetskv.passionwoman.app.presentation.base.ViewModelDependencies
import ru.babaetskv.passionwoman.app.presentation.feature.InDevelopmentFragment
import ru.babaetskv.passionwoman.app.presentation.feature.catalog.CatalogFragment
import ru.babaetskv.passionwoman.app.presentation.feature.home.HomeFragment
import ru.babaetskv.passionwoman.app.presentation.feature.profile.ProfileFragment
import ru.babaetskv.passionwoman.app.utils.deeplink.DeeplinkPayload
import ru.babaetskv.passionwoman.domain.interactor.SyncFavoritesUseCase
import ru.babaetskv.passionwoman.domain.preferences.AuthPreferences

class NavigationViewModel(
    args: NavigationFragment.Args,
    authPreferences: AuthPreferences,
    private val syncFavoritesUseCase: SyncFavoritesUseCase,
    dependencies: ViewModelDependencies
) : BaseViewModel<NavigationViewModel.Router>(dependencies) {
    private val authTypeFlow = authPreferences.authTypeFlow.onEach(::onAuthTypeUpdated)

    val selectedTabLiveData = MutableLiveData(Tab.HOME)
    val dialogLiveData = MutableLiveData<Dialog?>()

    override val logScreenOpening: Boolean = false

    init {
        authTypeFlow.launchIn(this)
        args.payload?.let {
            when (it) {
                is DeeplinkPayload.Product -> launch {
                    navigateTo(Router.ProductScreen(it.productId))
                }
            }
        }
    }

    private suspend fun onAuthTypeUpdated(authType: AuthPreferences.AuthType) {
        when (authType) {
            AuthPreferences.AuthType.NONE -> navigateTo(Router.AuthScreen)
            AuthPreferences.AuthType.AUTHORIZED -> syncFavorites()
            else -> Unit
        }
    }

    private suspend fun syncFavorites() {
        // TODO: reset sync later if it failed
        syncFavoritesUseCase.execute(SyncFavoritesUseCase.Params { doOnAnswer ->
            dialogLiveData.postValue(Dialog.MergeFavorites {
                dialogLiveData.postValue(null)
                launch {
                    doOnAnswer.invoke(it)
                }
            })
        })
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

    sealed class Router : RouterEvent {

        object AuthScreen : Router()

        data class ProductScreen(
            val productId: String
        ) : Router()
    }

    sealed class Dialog {

        data class MergeFavorites(
            val callback: (merge: Boolean) -> Unit
        ) : Dialog()
    }
}
