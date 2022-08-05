package ru.babaetskv.passionwoman.domain.model.base

interface Transformable<P : Any, T : Any?> {
    suspend fun transform(params: P) : T
}
