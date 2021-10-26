package ru.babaetskv.passionwoman.domain.interactor.base

import android.util.Log

abstract class BaseUseCase<in P, out R> {

    protected abstract suspend fun run(params: P): R

    protected abstract fun getUseCaseException(cause: Exception): Exception

    suspend fun execute(params: P): R = try {
        Log.i("Interactor", "Executing ${this::class.simpleName}") // TODO: replace with timber
        run(params)
    } catch (e: Exception) {
        throw getUseCaseException(e)
    }
}
