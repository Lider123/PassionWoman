package ru.babaetskv.passionwoman.app.analytics.base

import android.os.Bundle
import ru.babaetskv.passionwoman.app.analytics.constants.EventKeys
import ru.babaetskv.passionwoman.app.analytics.constants.ParamsKeys

// TODO: add events: checkout, search
interface AnalyticsEvent {
    fun getName(keys: EventKeys): String
    fun getParams(keys: ParamsKeys): Bundle
}
