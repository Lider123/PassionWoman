package ru.babaetskv.passionwoman.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Color(
    val name: String,
    val hex: String
) : Parcelable {
    val isMulticolor: Boolean
        get() = hex == MULTICOLOR

    companion object {
        private const val MULTICOLOR = "multicolor"
    }
}
