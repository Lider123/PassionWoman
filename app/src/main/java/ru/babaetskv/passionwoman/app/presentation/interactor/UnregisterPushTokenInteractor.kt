package ru.babaetskv.passionwoman.app.presentation.interactor

import androidx.work.*
import ru.babaetskv.passionwoman.app.presentation.interactor.base.BaseInteractor
import ru.babaetskv.passionwoman.app.presentation.worker.UnregisterPushTokenWorker
import ru.babaetskv.passionwoman.domain.StringProvider
import ru.babaetskv.passionwoman.domain.exceptions.UseCaseException
import ru.babaetskv.passionwoman.domain.usecase.UnregisterPushTokenUseCase
import java.util.concurrent.TimeUnit

class UnregisterPushTokenInteractor(
    private val workManager: WorkManager,
    private val stringProvider: StringProvider
) : BaseInteractor<Unit, Unit>(), UnregisterPushTokenUseCase {
    override val emptyException: UseCaseException.EmptyData? = null

    override suspend fun run(params: Unit) {
        val request = OneTimeWorkRequestBuilder<UnregisterPushTokenWorker>()
            .setBackoffCriteria(BackoffPolicy.LINEAR, 10L, TimeUnit.SECONDS)
            .build()
        workManager.enqueue(request)
    }

    override fun transformException(cause: Exception): UseCaseException =
        UnregisterPushTokenUseCase.UnregisterPushToken(cause, stringProvider)
}