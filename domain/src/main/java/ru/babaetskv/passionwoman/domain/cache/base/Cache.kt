package ru.babaetskv.passionwoman.domain.cache.base

interface Cache<T> {

    fun get(): T?
    fun set(value: T)
    fun clear()
}
