package ru.babaetskv.passionwoman.domain.model.base

abstract class PagedResponse<T>(items: List<T>) : List<T> by items {
    abstract val total: Int
}
