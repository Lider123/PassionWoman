package ru.babaetskv.passionwoman.app.analytics.event

import android.os.Bundle
import androidx.core.os.bundleOf
import ru.babaetskv.passionwoman.app.analytics.constants.ParamsKeys
import ru.babaetskv.passionwoman.app.analytics.event.base.SelectListItemEvent
import ru.babaetskv.passionwoman.domain.model.Brand

class SelectBrandEvent(
    private val brand: Brand
) : SelectListItemEvent(ListItemType.BRAND) {
    override val commonParams: CommonParams
        get() = brand.let {
            CommonParams(
                id = it.id,
                name = it.name
            )
        }

    override fun getAdditionalParams(keys: ParamsKeys): Bundle = bundleOf()
}
