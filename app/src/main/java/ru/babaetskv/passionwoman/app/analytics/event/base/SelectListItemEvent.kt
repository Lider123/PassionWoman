package ru.babaetskv.passionwoman.app.analytics.event.base

import android.os.Bundle
import androidx.core.os.bundleOf
import ru.babaetskv.passionwoman.app.analytics.base.AnalyticsEvent
import ru.babaetskv.passionwoman.app.analytics.constants.EventKeys
import ru.babaetskv.passionwoman.app.analytics.constants.ParamsKeys
import ru.babaetskv.passionwoman.app.utils.plus

abstract class SelectListItemEvent(
    protected val type: ListItemType
) : AnalyticsEvent {
    protected abstract val commonParams: CommonParams

    protected abstract fun getAdditionalParams(keys: ParamsKeys): Bundle

    override fun getName(keys: EventKeys): String = keys.SELECT_ITEM

    override fun getParams(keys: ParamsKeys): Bundle =
        getCommonParams(keys) + getAdditionalParams(keys)

    private fun getCommonParams(keys: ParamsKeys): Bundle =
        bundleOf(
            keys.ITEM_ID to commonParams.id,
            keys.ITEM_NAME to commonParams.name,
            keys.CONTENT_TYPE to type
        )

    enum class ListItemType(val paramName: String) {
        PRODUCT("product"),
        CATEGORY("category"),
        BRAND("brand")
    }

    data class CommonParams(
        val id: Long,
        val name: String
    )
}
