package ru.babaetskv.passionwoman.app.presentation.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import ru.babaetskv.passionwoman.app.presentation.worker.base.ChildWorkerFactory
import ru.babaetskv.passionwoman.domain.gateway.PushGateway
import ru.babaetskv.passionwoman.domain.preferences.AppPreferences
import timber.log.Timber

class RegisterPushTokenWorker(
    private val appPreferences: AppPreferences,
    private val pushGateway: PushGateway,
    context: Context,
    workerParams: WorkerParameters,
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result =
        try {
            val token = appPreferences.pushToken
            pushGateway.registerPushToken(token)
            Timber.i("Successfully registered push token")
            Result.success()
        } catch (e: Exception) {
            if (runAttemptCount + 1 >= MAX_ATTEMPT_COUNT) {
                Timber.i("Failed to register push token")
                Result.failure()
            } else {
                Timber.i("Failed to register push token. Retrying")
                Result.retry()
            }
        }

    class Factory(
        private val appPreferences: AppPreferences,
        private val pushGateway: PushGateway
    ) : ChildWorkerFactory {

        override fun create(appContext: Context, params: WorkerParameters): CoroutineWorker =
            RegisterPushTokenWorker(appPreferences, pushGateway, appContext, params)
    }

    companion object {
        private const val MAX_ATTEMPT_COUNT = 3
    }
}
