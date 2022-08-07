package ru.babaetskv.passionwoman.app.presentation.interactor.base

import ru.babaetskv.passionwoman.domain.exceptions.UseCaseException
import ru.babaetskv.passionwoman.domain.usecase.base.UseCase
import timber.log.Timber

abstract class BaseInteractor<in P, out R> : UseCase<P, R> {
    protected abstract val emptyException: UseCaseException.EmptyData?

    protected abstract suspend fun run(params: P): R
    protected abstract fun transformException(cause: Exception): UseCaseException

    override suspend fun execute(params: P): R {
        val result = try {
            Timber.tag(TAG).i("Executing ${this::class.simpleName}")
            run(params)
        } catch (e: Exception) {
            Timber.tag(TAG).w("Failed to execute ${this::class.simpleName}")
            throw transformException(e)
        }
        if (result is Collection<*> && result.isEmpty()) {
            emptyException?.let { throw it }
        }

        return result
    }

    companion object {
        private const val TAG = "Interactor"
    }
}
