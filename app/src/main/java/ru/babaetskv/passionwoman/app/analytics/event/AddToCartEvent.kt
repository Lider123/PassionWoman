package ru.babaetskv.passionwoman.app.analytics.event

import android.os.Bundle
import androidx.core.os.bundleOf
import ru.babaetskv.passionwoman.app.analytics.base.AnalyticsEvent
import ru.babaetskv.passionwoman.app.analytics.constants.EventKeys
import ru.babaetskv.passionwoman.app.analytics.constants.ParamsKeys
import ru.babaetskv.passionwoman.domain.model.Product

class AddToCartEvent(
    private val product: Product
) : AnalyticsEvent {

    override fun getName(keys: EventKeys): String = keys.ADD_TO_CART

    override fun getParams(keys: ParamsKeys): Bundle =
        bundleOf(
            keys.ITEM_ID to product.id,
            keys.ITEM_NAME to product.name
        )
}
