package ru.babaetskv.passionwoman.app.presentation.base

import androidx.paging.InvalidatingPagingSourceFactory
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingSource

class SimplePager<Key : Any, Value : Any>(
    pageSize: Int,
    pagingSourceFactory: () -> PagingSource<Key, Value>
) {
    private val invalidatingPagingSourceFactory = InvalidatingPagingSourceFactory(pagingSourceFactory)
    private val pager = Pager(
        config = PagingConfig(
            pageSize = pageSize,
            initialLoadSize = pageSize
        ),
        pagingSourceFactory = invalidatingPagingSourceFactory
    )

    val flow = pager.flow

    fun invalidate() = invalidatingPagingSourceFactory.invalidate()
}