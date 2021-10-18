package ru.babaetskv.passionwoman.app

import ru.babaetskv.passionwoman.app.analytics.AnalyticService

interface ServiceProvider {
    fun provideAnalyticService(): AnalyticService
}
