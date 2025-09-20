package ru.babaetskv.passionwoman.domain.model.base

abstract class Transformable<P : Any, T : Any?> {

    abstract suspend fun transform(params: P) : T

    companion object {

        suspend fun <T : Any?> Transformable<Unit, T>.transform(): T = transform(Unit)

        suspend fun <T: Any?> List<Transformable<Unit, T>>.transformList(): List<T> = map { it.transform() }

        suspend fun <P : Any, T: Any?> List<Transformable<P, T>>.transformList(params: P): List<T> =
            map { it.transform(params) }

        suspend fun <T: Any> List<Transformable<Unit, T?>>.transformListNotNull(): List<T> =
            mapNotNull { it.transform() }
    }
}
