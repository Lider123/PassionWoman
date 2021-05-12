package ru.babaetskv.passionwoman.domain.interactor.base

abstract class BaseUseCase<in P, out R> {

    protected abstract suspend fun run(params: P): R

    suspend fun execute(params: P): Result<R> = try {
        val data = run(params)
        Result.Success(data)
    } catch(e: Exception) {
        Result.Failure(e)
    }

    sealed class Result<out R> {

        data class Success<out T>(
            val data: T
        ) : Result<T>()

        data class Failure(
            val error: Throwable
        ) : Result<Nothing>()
    }
}