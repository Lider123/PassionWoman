package ru.babaetskv.passionwoman.domain.interactor.base

abstract class BaseUseCase<in P, out R> {

    protected abstract suspend fun run(params: P): R

    protected abstract fun getUseCaseException(cause: Exception): Exception

    suspend fun execute(params: P): R = try {
        run(params)
    } catch (e: Exception) {
        throw getUseCaseException(e)
    }
}
