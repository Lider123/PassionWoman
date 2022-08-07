package ru.babaetskv.passionwoman.app.presentation.event

interface RouterEvent : Event {
    object GoBack : RouterEvent
    object LogIn : RouterEvent
}
