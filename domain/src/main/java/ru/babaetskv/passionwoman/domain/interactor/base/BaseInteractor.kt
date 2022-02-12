package ru.babaetskv.passionwoman.domain.interactor.base

import ru.babaetskv.passionwoman.domain.usecase.base.UseCase
import timber.log.Timber

abstract class BaseInteractor<in P, out R> : UseCase<P, R> {

    protected abstract suspend fun run(params: P): R
    protected abstract fun getUseCaseException(cause: Exception): Exception

    override suspend fun execute(params: P): R = try {
        Timber.tag(TAG).i("Executing ${this::class.simpleName}")
        run(params)
    } catch (e: Exception) {
        Timber.tag(TAG).w("Failed to execute ${this::class.simpleName}")
        throw getUseCaseException(e)
    }

    companion object {
        private const val TAG = "Interactor"
    }
}
