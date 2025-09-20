package ru.babaetskv.passionwoman.app.presentation.feature.profile

import androidx.lifecycle.MutableLiveData
import ru.babaetskv.passionwoman.app.navigation.ScreenProvider
import ru.babaetskv.passionwoman.app.presentation.base.ViewModelDependencies
import ru.babaetskv.passionwoman.app.presentation.feature.profile.menu.*
import ru.babaetskv.passionwoman.domain.StringProvider
import ru.babaetskv.passionwoman.domain.preferences.AuthPreferences
import ru.babaetskv.passionwoman.domain.usecase.GetProfileUseCase
import ru.babaetskv.passionwoman.domain.usecase.LogOutUseCase
import ru.babaetskv.passionwoman.domain.usecase.UnregisterPushTokenUseCase
import ru.babaetskv.passionwoman.domain.usecase.UpdateAvatarUseCase

class ProfileViewModelImpl(
    getProfileUseCase: GetProfileUseCase,
    authPreferences: AuthPreferences,
    logOutUseCase: LogOutUseCase,
    updateAvatarUseCase: UpdateAvatarUseCase,
    unregisterPushTokenUseCase: UnregisterPushTokenUseCase,
    stringProvider: StringProvider,
    dependencies: ViewModelDependencies
) : BaseProfileViewModelImpl(getProfileUseCase, authPreferences, logOutUseCase, updateAvatarUseCase, unregisterPushTokenUseCase, stringProvider, dependencies) {
    override val menuItemsLiveData = MutableLiveData(listOf(
        FavoritesProfileMenuItem(),
        OrdersProfileMenuItem(),
        ContactsProfileMenuItem(),
        DemoPresetsProfileMenuItem()
    ))

    override fun onMenuItemPressed(item: ProfileMenuItem) {
        when (item) {
            is DemoPresetsProfileMenuItem -> router.navigateTo(ScreenProvider.demo(false))
            else -> super.onMenuItemPressed(item)
        }
    }
}
