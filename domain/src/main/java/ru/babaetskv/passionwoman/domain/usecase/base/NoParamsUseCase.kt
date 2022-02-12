package ru.babaetskv.passionwoman.domain.usecase.base

interface NoParamsUseCase<out R> : UseCase<Unit, R> {
    suspend fun execute(): R = execute(Unit)
}
