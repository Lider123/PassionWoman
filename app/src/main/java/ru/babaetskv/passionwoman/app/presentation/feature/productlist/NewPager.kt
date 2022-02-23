package ru.babaetskv.passionwoman.app.presentation.feature.productlist

import androidx.paging.*
import ru.babaetskv.passionwoman.data.datasource.base.InvalidatingPagingSourceFactory
import ru.babaetskv.passionwoman.domain.model.base.PagedResponse

class NewPager<T : Any, R : PagedResponse<T>>(
    pageSize: Int,
    private val loadNext: suspend (limit: Int, offset: Int) -> R,
    private val doOnNextPageLoaded: (R) -> Unit,
) {
    private val pagingSource: PagingSource<Int, T>
        get() = object : PagingSource<Int, T>() {

            override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> {
                val currentPage = params.key ?: START_PAGE
                return try {
                    val limit = params.loadSize
                    val offset = limit * (currentPage - 1)
                    val response = loadNext.invoke(limit, offset)
                    if (response.isEmpty() && currentPage == START_PAGE) {
                        // TODO: throw EmptyListException()
                    }

                    doOnNextPageLoaded.invoke(response)
                    LoadResult.Page(
                        data = response,
                        prevKey = if (currentPage == START_PAGE) null else currentPage - 1,
                        nextKey = if (response.isNotEmpty()) currentPage + 1 else null
                    )
                } catch (e: Exception) {
                    LoadResult.Error(e)
                    // TODO: replace e with if (currentPage == START_PAGE) GetListException(e) else GetPageException(e)
                }
            }

            override fun getRefreshKey(state: PagingState<Int, T>): Int? = state.anchorPosition?.let {
                state.closestPageToPosition(it)?.prevKey?.plus(1)
                    ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
            }
        }
    private val pagingSourceFactory = object : InvalidatingPagingSourceFactory<Int, T>() {

        override fun create(): PagingSource<Int, T> = pagingSource
    }
    private val pager = Pager(
        config = PagingConfig(
            pageSize = pageSize,
            initialLoadSize = pageSize
        ),
        pagingSourceFactory = pagingSourceFactory
    )

    val flow = pager.flow

    fun invalidate() = pagingSourceFactory.invalidate()

    companion object {
        private const val START_PAGE = 1
    }
}
