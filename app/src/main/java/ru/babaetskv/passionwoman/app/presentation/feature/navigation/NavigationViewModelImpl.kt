package ru.babaetskv.passionwoman.app.presentation.feature.navigation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.babaetskv.passionwoman.app.navigation.Screens
import ru.babaetskv.passionwoman.app.presentation.base.BaseViewModel
import ru.babaetskv.passionwoman.app.presentation.base.ViewModelDependencies
import ru.babaetskv.passionwoman.app.utils.deeplink.DeeplinkPayload
import ru.babaetskv.passionwoman.domain.dataflow.CartFlow
import ru.babaetskv.passionwoman.domain.model.Cart
import ru.babaetskv.passionwoman.domain.preferences.AuthPreferences
import ru.babaetskv.passionwoman.domain.usecase.SyncCartUseCase
import ru.babaetskv.passionwoman.domain.usecase.SyncFavoritesUseCase
import kotlin.coroutines.CoroutineContext

class NavigationViewModelImpl(
    args: NavigationFragment.Args,
    authPreferences: AuthPreferences,
    private val syncFavoritesUseCase: SyncFavoritesUseCase,
    private val syncCartUseCase: SyncCartUseCase,
    private val cartFlow: CartFlow,
    dependencies: ViewModelDependencies
) : BaseViewModel(dependencies), NavigationViewModel {
    private val authTypeFlow = authPreferences.authTypeFlow.onEach(::onAuthTypeUpdated)
    private val mCartFlow: Flow<Cart>
        get() = cartFlow.subscribe()

    override val selectedTabLiveData = MutableLiveData(NavigationViewModel.Tab.HOME)
    override val dialogLiveData = MutableLiveData<NavigationViewModel.Dialog?>()
    override val cartLiveData: LiveData<Cart>
        get() = mCartFlow.asLiveData(coroutineContext)
    override val logScreenOpening: Boolean = false

    init {
        authTypeFlow.launchIn(this)
        mCartFlow.launchIn(this)
        args.payload?.let {
            when (it) {
                is DeeplinkPayload.Product -> router.navigateTo(Screens.productCard(it.productId))
            }
        }
    }

    override fun onError(context: CoroutineContext, error: Throwable) {
        when (error) {
            is CartFlow.EmptyCartException -> return
            else -> super.onError(context, error)
        }
    }

    override fun onTabPressed(tab: NavigationViewModel.Tab) {
        selectedTabLiveData.postValue(tab)
    }

    private fun onAuthTypeUpdated(authType: AuthPreferences.AuthType) {
        when (authType) {
            AuthPreferences.AuthType.NONE -> router.newRootScreen(Screens.auth(true))
            AuthPreferences.AuthType.AUTHORIZED -> launch {
                // TODO: make cart and favorites syncing parallel
                syncCart()
                syncFavorites()
            }
            else -> Unit
        }
    }

    private suspend fun syncFavorites() {
        // TODO: reset sync later if it failed
        syncFavoritesUseCase.execute(SyncFavoritesUseCase.Params { doOnAnswer ->
            dialogLiveData.postValue(NavigationViewModel.Dialog.MergeFavorites {
                dialogLiveData.postValue(null)
                launch {
                    doOnAnswer.invoke(it)
                }
            })
        })
    }

    private suspend fun syncCart() {
        syncCartUseCase.execute(Unit)
    }
}
