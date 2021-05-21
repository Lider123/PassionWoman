package ru.babaetskv.passionwoman.app.presentation.feature.splash

import com.github.terrakok.cicerone.Router
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.babaetskv.passionwoman.app.Screens
import ru.babaetskv.passionwoman.app.presentation.base.BaseViewModel
import ru.babaetskv.passionwoman.app.utils.notifier.Notifier

class SplashViewModel(
    notifier: Notifier,
    router: Router
) : BaseViewModel(notifier, router) {

    override fun onResume() {
        super.onResume()
        launch {
            delay(DELAY)
            router.replaceScreen(Screens.navigation())
        }
    }

    companion object {
        private const val DELAY = 2300L
    }
}
