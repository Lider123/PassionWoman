package ru.babaetskv.passionwoman.domain.usecase.base

interface UseCase<in P, out R> {
    suspend fun execute(params: P) : R

    companion object {
        suspend fun <R> UseCase<Unit, R>.execute(): R = execute(Unit)
    }
}
