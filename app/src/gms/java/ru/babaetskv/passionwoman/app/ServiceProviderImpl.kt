package ru.babaetskv.passionwoman.app

import ru.babaetskv.passionwoman.app.analytics.AnalyticService
import ru.babaetskv.passionwoman.app.analytics.GoogleAnalyticService

class ServiceProviderImpl : ServiceProvider {

    override fun provideAnalyticService(): AnalyticService = GoogleAnalyticService()
}
