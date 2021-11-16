package ru.babaetskv.passionwoman.app.presentation.base

import androidx.paging.Pager
import androidx.paging.PagingConfig
import ru.babaetskv.passionwoman.data.datasource.base.InvalidatingPagingSourceFactory

abstract class SimplePager<Key : Any, Value : Any>(
    pageSize: Int,
    protected val pagingSourceFactory: InvalidatingPagingSourceFactory<Key, Value>
) {
    private val pager = Pager(
        config = PagingConfig(
            pageSize = pageSize,
            initialLoadSize = pageSize
        ),
        pagingSourceFactory = pagingSourceFactory
    )

    val flow = pager.flow

    fun invalidate() = pagingSourceFactory.invalidate()
}