package ru.babaetskv.passionwoman.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Deprecated("Use StaticFilters instead")
@Parcelize
data class Filters(
    val discountOnly: Boolean
) : Parcelable {

    companion object {
        val DEFAULT = Filters(
            discountOnly = false
        )
    }
}
