package ru.babaetskv.passionwoman.domain.model.base

interface Transformable<P : Any, T : Any?> {
    fun transform(params: P) : T
}
