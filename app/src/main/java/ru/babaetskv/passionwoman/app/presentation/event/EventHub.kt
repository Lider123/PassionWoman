package ru.babaetskv.passionwoman.app.presentation.event

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class EventHub {
    private val eventsFlow = MutableSharedFlow<Event>()

    val flow: Flow<Event>
        get() = eventsFlow.asSharedFlow()

    suspend fun post(event: Event) {
        eventsFlow.emit(event)
    }
}
