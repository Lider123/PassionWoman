package ru.babaetskv.passionwoman.domain.utils

import ru.babaetskv.passionwoman.domain.interactor.base.BaseUseCase

suspend fun <T> BaseUseCase<Unit, T>.execute(): T = execute(Unit)
