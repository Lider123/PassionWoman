package ru.babaetskv.passionwoman.app.analytics.event

import android.os.Bundle
import androidx.core.os.bundleOf
import ru.babaetskv.passionwoman.app.analytics.base.AnalyticsEvent
import ru.babaetskv.passionwoman.app.analytics.constants.EventKeys
import ru.babaetskv.passionwoman.app.analytics.constants.ParamsKeys

class LoginEvent : AnalyticsEvent {

    override fun getName(keys: EventKeys): String = keys.LOGIN

    override fun getParams(keys: ParamsKeys): Bundle = bundleOf()
}
