package ru.babaetskv.passionwoman.app.analytics.base

interface AnalyticsHandler {

    fun log(event: AnalyticsEvent)
}
