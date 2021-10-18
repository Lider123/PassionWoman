package ru.babaetskv.passionwoman.app

import ru.babaetskv.passionwoman.app.analytics.AnalyticService
import ru.babaetskv.passionwoman.app.analytics.HuaweiAnalyticsService

class ServiceProviderImpl : ServiceProvider {

    override fun provideAnalyticService(): AnalyticService = HuaweiAnalyticsService()
}
