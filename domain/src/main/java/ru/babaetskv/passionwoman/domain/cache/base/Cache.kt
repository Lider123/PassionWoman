package ru.babaetskv.passionwoman.domain.cache.base

import androidx.lifecycle.LiveData

interface Cache<T> {

    val liveData: LiveData<T>

    fun get(): T?
    fun set(value: T)
    fun clear()
}
