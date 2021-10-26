package ru.babaetskv.passionwoman.app.presentation.base

import ru.babaetskv.passionwoman.app.analytics.base.AnalyticsHandler
import ru.babaetskv.passionwoman.app.utils.notifier.Notifier

class ViewModelDependencies(
    val notifier: Notifier,
    val analyticsHandler: AnalyticsHandler
)
