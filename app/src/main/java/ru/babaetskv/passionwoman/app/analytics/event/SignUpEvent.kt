package ru.babaetskv.passionwoman.app.analytics.event

import android.os.Bundle
import androidx.core.os.bundleOf
import ru.babaetskv.passionwoman.app.analytics.base.AnalyticsEvent
import ru.babaetskv.passionwoman.app.analytics.constants.EventKeys
import ru.babaetskv.passionwoman.app.analytics.constants.ParamsKeys

class SignUpEvent : AnalyticsEvent {

    override fun getName(keys: EventKeys): String = keys.SIGN_UP

    override fun getParams(keys: ParamsKeys): Bundle = bundleOf()
}
