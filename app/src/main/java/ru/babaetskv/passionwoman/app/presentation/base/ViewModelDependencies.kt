package ru.babaetskv.passionwoman.app.presentation.base

import ru.babaetskv.passionwoman.app.AppConfig
import ru.babaetskv.passionwoman.app.analytics.base.AnalyticsHandler
import ru.babaetskv.passionwoman.app.analytics.base.ErrorLogger
import ru.babaetskv.passionwoman.app.navigation.AppRouter
import ru.babaetskv.passionwoman.app.presentation.event.EventHub
import ru.babaetskv.passionwoman.app.utils.NetworkStateChecker
import ru.babaetskv.passionwoman.app.utils.notifier.Notifier

class ViewModelDependencies(
    val notifier: Notifier,
    val analyticsHandler: AnalyticsHandler,
    val errorLogger: ErrorLogger,
    val eventHub: EventHub,
    val router: AppRouter,
    val config: AppConfig,
    val networkStateChecker: NetworkStateChecker
)
