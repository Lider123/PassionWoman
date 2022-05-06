package ru.babaetskv.passionwoman.domain.cache.base

import kotlinx.coroutines.flow.Flow

interface Cache<T> {

    val flow: Flow<T>

    suspend fun get(): T?
    suspend fun set(value: T)
    suspend fun clear()
}
