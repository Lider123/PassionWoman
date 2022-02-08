package ru.babaetskv.passionwoman.domain.utils

import ru.babaetskv.passionwoman.domain.model.base.Transformable

fun <T : Any?> Transformable<Unit, T>.transform(): T = transform(Unit)

fun <T: Any?> List<Transformable<Unit, T>>.transformList(): List<T> = map { it.transform() }

fun <T: Any> List<Transformable<Unit, T?>>.transformListNotNull(): List<T> = mapNotNull { it.transform() }
