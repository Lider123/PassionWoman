package ru.babaetskv.passionwoman.domain.cache.base

interface ListCache<T> : Cache<List<T>> {

    suspend fun add(item: T)
    suspend fun remove(item: T)
}
