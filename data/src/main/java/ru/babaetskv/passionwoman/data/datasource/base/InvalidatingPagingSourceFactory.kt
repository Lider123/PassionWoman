package ru.babaetskv.passionwoman.data.datasource.base

import androidx.paging.PagingSource

abstract class InvalidatingPagingSourceFactory<Key: Any, Value: Any> : () -> PagingSource<Key, Value> {
    private var activeDataSources: MutableList<PagingSource<Key, Value>> = mutableListOf()

    protected abstract fun create(): PagingSource<Key, Value>

    override fun invoke(): PagingSource<Key, Value> =
        create().also {
            activeDataSources.add(it)
        }

    fun invalidate() {
        activeDataSources.forEach {
            it.invalidate()
        }
        activeDataSources = activeDataSources.filter { !it.invalid }.toMutableList()
    }
}
