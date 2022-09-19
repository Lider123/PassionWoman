package ru.babaetskv.passionwoman.domain

import kotlinx.coroutines.CoroutineDispatcher

interface AppDispatchers {
    val Main: CoroutineDispatcher
    val IO: CoroutineDispatcher
    val Default: CoroutineDispatcher
}
