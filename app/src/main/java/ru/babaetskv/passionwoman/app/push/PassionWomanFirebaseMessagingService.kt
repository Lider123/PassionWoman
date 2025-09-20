package ru.babaetskv.passionwoman.app.push

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.*
import org.koin.android.ext.android.inject
import ru.babaetskv.passionwoman.domain.preferences.AppPreferences
import ru.babaetskv.passionwoman.domain.usecase.RegisterPushTokenUseCase
import ru.babaetskv.passionwoman.domain.usecase.base.UseCase.Companion.execute
import timber.log.Timber
import kotlin.coroutines.CoroutineContext

class PassionWomanFirebaseMessagingService : FirebaseMessagingService(), CoroutineScope {
    private val registerPushTokenUseCase: RegisterPushTokenUseCase by inject()
    private val appPreferences: AppPreferences by inject()
    private val notificationManager: AppNotificationManager by inject()
    private val job = SupervisorJob()

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Default

    override fun onCreate() {
        super.onCreate()
        Timber.i("FCM token: ${appPreferences.pushToken}")
    }

    override fun onDestroy() {
        job.cancel()
        super.onDestroy()
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Timber.i("New FCM token: $token")
        appPreferences.pushToken = token
        launch {
            registerPushTokenUseCase.execute()
        }
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Timber.i("New message received")
        notificationManager.showNotification(message)
    }
}
