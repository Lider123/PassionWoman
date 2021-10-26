package ru.babaetskv.passionwoman.app.analytics.base

interface ErrorLogger {
    fun logException(t: Throwable)
}
