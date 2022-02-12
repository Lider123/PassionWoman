package ru.babaetskv.passionwoman.app.presentation.feature.navigation

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.babaetskv.passionwoman.app.presentation.base.BaseViewModel
import ru.babaetskv.passionwoman.app.presentation.base.ViewModelDependencies
import ru.babaetskv.passionwoman.app.utils.deeplink.DeeplinkPayload
import ru.babaetskv.passionwoman.domain.preferences.AuthPreferences
import ru.babaetskv.passionwoman.domain.usecase.SyncFavoritesUseCase

class NavigationViewModelImpl(
    args: NavigationFragment.Args,
    authPreferences: AuthPreferences,
    private val syncFavoritesUseCase: SyncFavoritesUseCase,
    dependencies: ViewModelDependencies
) : BaseViewModel<NavigationViewModel.Router>(dependencies), NavigationViewModel {
    private val authTypeFlow = authPreferences.authTypeFlow.onEach(::onAuthTypeUpdated)

    override val selectedTabLiveData = MutableLiveData(NavigationViewModel.Tab.HOME)
    override val dialogLiveData = MutableLiveData<NavigationViewModel.Dialog?>()
    override val logScreenOpening: Boolean = false

    init {
        authTypeFlow.launchIn(this)
        args.payload?.let {
            when (it) {
                is DeeplinkPayload.Product -> launch {
                    navigateTo(NavigationViewModel.Router.ProductScreen(it.productId))
                }
            }
        }
    }

    override fun onTabPressed(tab: NavigationViewModel.Tab) {
        selectedTabLiveData.postValue(tab)
    }

    private suspend fun onAuthTypeUpdated(authType: AuthPreferences.AuthType) {
        when (authType) {
            AuthPreferences.AuthType.NONE -> navigateTo(NavigationViewModel.Router.AuthScreen)
            AuthPreferences.AuthType.AUTHORIZED -> syncFavorites()
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
}
