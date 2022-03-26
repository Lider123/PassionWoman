package ru.babaetskv.passionwoman.domain.cache.base

interface ListCache<T> : Cache<List<T>> {

    fun add(item: T)
    fun remove(item: T)
}
