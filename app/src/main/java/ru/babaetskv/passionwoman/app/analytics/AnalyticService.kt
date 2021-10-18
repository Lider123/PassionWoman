package ru.babaetskv.passionwoman.app.analytics

import android.app.Application

interface AnalyticService {
    val isEnabled: Boolean

    fun init(app: Application)
    fun log(event: AnalyticEvent)
}
