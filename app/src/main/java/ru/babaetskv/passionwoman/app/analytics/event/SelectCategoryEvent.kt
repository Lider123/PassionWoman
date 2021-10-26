package ru.babaetskv.passionwoman.app.analytics.event

import android.os.Bundle
import androidx.core.os.bundleOf
import ru.babaetskv.passionwoman.app.analytics.constants.ParamsKeys
import ru.babaetskv.passionwoman.app.analytics.event.base.SelectListItemEvent
import ru.babaetskv.passionwoman.domain.model.Category

class SelectCategoryEvent(
    private val category: Category
) : SelectListItemEvent(ListItemType.CATEGORY) {
    override val commonParams: CommonParams
        get() = category.let {
            CommonParams(
                id = it.id,
                name = it.name
            )
        }

    override fun getAdditionalParams(keys: ParamsKeys): Bundle = bundleOf()
}
