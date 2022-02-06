package ru.babaetskv.passionwoman.app.presentation.event

import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow

class EventHub {
    private val channel = BroadcastChannel<InnerEvent>(Channel.BUFFERED)

    val flow: Flow<InnerEvent>
        get() = channel.openSubscription().receiveAsFlow()

    suspend fun post(event: InnerEvent) {
        channel.send(event)
    }
}
