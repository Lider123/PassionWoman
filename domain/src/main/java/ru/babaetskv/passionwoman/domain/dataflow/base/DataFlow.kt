package ru.babaetskv.passionwoman.domain.dataflow.base

import kotlinx.coroutines.flow.Flow

interface DataFlow<T> {
    fun subscribe(): Flow<T>
    suspend fun clear()
    suspend fun send(data: T)
}
