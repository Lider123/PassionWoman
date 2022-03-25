package ru.babaetskv.passionwoman.app.analytics

import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import ru.babaetskv.passionwoman.app.BuildConfig
import ru.babaetskv.passionwoman.app.analytics.base.AnalyticsEvent
import ru.babaetskv.passionwoman.app.analytics.base.AnalyticsHandler
import ru.babaetskv.passionwoman.app.analytics.constants.EventKeys
import ru.babaetskv.passionwoman.app.analytics.constants.ParamsKeys
import ru.babaetskv.passionwoman.domain.preferences.AuthPreferences

class FirebaseAnalyticsHandler(
    private val authPreferences: AuthPreferences
) : AnalyticsHandler {
    private val analytics = Firebase.analytics.apply {
        setAnalyticsCollectionEnabled(!BuildConfig.DEBUG)
    }

    override fun log(event: AnalyticsEvent) {
        analytics.logEvent(
            event.getName(FirebaseEventKeys),
            event.getParams(FirebaseParamsKeys).apply {
                putString(FirebaseParamsKeys.USER_ID, authPreferences.userId)
            }
        )
    }

    private object FirebaseEventKeys : EventKeys {
        override val SELECT_ITEM: String = FirebaseAnalytics.Event.SELECT_ITEM
        override val SCREEN_VIEW: String = FirebaseAnalytics.Event.SCREEN_VIEW
        override val LOGIN: String = FirebaseAnalytics.Event.LOGIN
        override val SIGN_UP: String = FirebaseAnalytics.Event.SIGN_UP
        override val ADD_TO_WISHLIST: String = FirebaseAnalytics.Event.ADD_TO_WISHLIST
        override val ADD_TO_CART: String = FirebaseAnalytics.Event.ADD_TO_CART
    }

    private object FirebaseParamsKeys : ParamsKeys {
        override val ITEM_ID: String = FirebaseAnalytics.Param.ITEM_ID
        override val ITEM_NAME: String = FirebaseAnalytics.Param.ITEM_NAME
        override val CONTENT_TYPE: String = FirebaseAnalytics.Param.CONTENT_TYPE
        override val SCREEN_NAME: String = FirebaseAnalytics.Param.SCREEN_NAME
        override val USER_ID: String = "user_id"
        override val BRAND_NAME: String = FirebaseAnalytics.Param.ITEM_BRAND
        override val CATEGORY_NAME: String = FirebaseAnalytics.Param.ITEM_CATEGORY
    }
}
