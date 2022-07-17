package ru.babaetskv.passionwoman.domain.dataflow.base

import kotlinx.coroutines.flow.Flow

interface DataFlow<T> {
    val flow: Flow<T>

    suspend fun send(data: T)
}
