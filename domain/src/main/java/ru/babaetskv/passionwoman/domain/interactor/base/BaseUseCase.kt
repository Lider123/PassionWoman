package ru.babaetskv.passionwoman.domain.interactor.base

import timber.log.Timber

abstract class BaseUseCase<in P, out R> {

    protected abstract suspend fun run(params: P): R

    protected abstract fun getUseCaseException(cause: Exception): Exception

    suspend fun execute(params: P): R = try {
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
