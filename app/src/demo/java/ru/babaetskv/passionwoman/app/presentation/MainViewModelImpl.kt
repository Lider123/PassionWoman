package ru.babaetskv.passionwoman.app.presentation

import ru.babaetskv.passionwoman.app.navigation.ScreenProvider
import ru.babaetskv.passionwoman.app.presentation.base.ViewModelDependencies
import ru.babaetskv.passionwoman.app.utils.deeplink.DeeplinkHandler
import ru.babaetskv.passionwoman.app.utils.deeplink.DeeplinkPayload
import ru.babaetskv.passionwoman.domain.preferences.AppPreferences
import ru.babaetskv.passionwoman.domain.preferences.AuthPreferences
import ru.babaetskv.passionwoman.domain.usecase.GetProfileUseCase
import ru.babaetskv.passionwoman.domain.usecase.InitDatabaseUseCase
import ru.babaetskv.passionwoman.domain.usecase.base.UseCase.Companion.execute

class MainViewModelImpl(
    private val initDatabaseUseCase: InitDatabaseUseCase,
    deeplinkHandler: DeeplinkHandler,
    appPrefs: AppPreferences,
    authPrefs: AuthPreferences,
    getProfileUseCase: GetProfileUseCase,
    dependencies: ViewModelDependencies
) : BaseMainViewModelImpl(deeplinkHandler, authPrefs, appPrefs, getProfileUseCase, dependencies) {

    override fun prepareApp() {
        launchWithLoading {
            if (!appPrefs.databaseFilled) initDatabaseUseCase.execute()
            super.prepareApp()
        }
    }

    override fun navigateOnStart(payload: DeeplinkPayload?) {
        when {
            !appPrefs.demoOnStartShowed -> router.newRootScreen(ScreenProvider.demo(true))
            else -> super.navigateOnStart(payload)
        }
    }
}
