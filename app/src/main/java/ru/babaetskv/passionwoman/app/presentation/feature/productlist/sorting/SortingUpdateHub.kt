package ru.babaetskv.passionwoman.app.presentation.feature.productlist.sorting

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import ru.babaetskv.passionwoman.domain.model.Sorting

class SortingUpdateHub {
    private val channel = Channel<Sorting>(Channel.RENDEZVOUS)

    val flow: Flow<Sorting> = channel.receiveAsFlow()

    suspend fun post(sorting: Sorting) {
        channel.send(sorting)
    }
}