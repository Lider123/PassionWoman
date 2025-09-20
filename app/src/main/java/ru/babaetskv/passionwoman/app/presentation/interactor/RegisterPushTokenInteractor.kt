package ru.babaetskv.passionwoman.app.presentation.interactor

import androidx.work.*
import ru.babaetskv.passionwoman.app.presentation.interactor.base.BaseInteractor
import ru.babaetskv.passionwoman.app.presentation.worker.RegisterPushTokenWorker
import ru.babaetskv.passionwoman.domain.StringProvider
import ru.babaetskv.passionwoman.domain.exceptions.UseCaseException
import ru.babaetskv.passionwoman.domain.usecase.RegisterPushTokenUseCase
import java.util.concurrent.TimeUnit

class RegisterPushTokenInteractor(
    private val workManager: WorkManager,
    private val stringProvider: StringProvider
) : BaseInteractor<Unit, Unit>(),  RegisterPushTokenUseCase {
    override val emptyException: UseCaseException.EmptyData? = null

    override suspend fun run(params: Unit) {
        val request = OneTimeWorkRequestBuilder<RegisterPushTokenWorker>()
            .setBackoffCriteria(BackoffPolicy.LINEAR, 10L, TimeUnit.SECONDS)
            .build()
        workManager.enqueue(request)
    }

    override fun transformException(cause: Exception): UseCaseException =
        RegisterPushTokenUseCase.RegisterPushTokenException(cause, stringProvider)
}
