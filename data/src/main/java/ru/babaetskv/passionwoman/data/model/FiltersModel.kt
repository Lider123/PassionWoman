package ru.babaetskv.passionwoman.data.model

import com.squareup.moshi.Json
import ru.babaetskv.passionwoman.domain.model.Filters

data class FiltersModel(
    @Json(name = "discountOnly") val discountOnly: Boolean
) {

    fun toFilters() =
        Filters(
            discountOnly = discountOnly
        )

    companion object {

        fun fromFilters(filters: Filters) =
            FiltersModel(
                discountOnly = filters.discountOnly
            )
    }
}
