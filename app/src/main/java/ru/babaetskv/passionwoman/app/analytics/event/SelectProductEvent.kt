package ru.babaetskv.passionwoman.app.analytics.event

import android.os.Bundle
import androidx.core.os.bundleOf
import ru.babaetskv.passionwoman.app.analytics.constants.ParamsConstants
import ru.babaetskv.passionwoman.app.analytics.constants.ParamsKeys
import ru.babaetskv.passionwoman.app.analytics.event.base.SelectListItemEvent
import ru.babaetskv.passionwoman.domain.model.Product

class SelectProductEvent(
    private val product: Product
) : SelectListItemEvent(ListItemType.PRODUCT) {
    override val commonParams: CommonParams
        get() = product.let {
            CommonParams(
                id = it.id,
                name = it.name
            )
        }

    override fun getAdditionalParams(keys: ParamsKeys): Bundle =
        with (product) {
            bundleOf(
                keys.CATEGORY_NAME to product.category.name,
                keys.BRAND_NAME to (product.brand?.name ?: ParamsConstants.UNKNOWN)
            )
        }
}
