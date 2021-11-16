package ru.babaetskv.passionwoman.app.presentation.feature.productlist.filters

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import ru.babaetskv.passionwoman.domain.model.filters.Filter

// TODO: think about setting up a general event hub
class FiltersUpdateHub {
    private val channel = Channel<List<Filter>>(Channel.RENDEZVOUS)

    val flow: Flow<List<Filter>> = channel.receiveAsFlow()

    suspend fun post(filters: List<Filter>) {
        channel.send(filters)
    }
}
