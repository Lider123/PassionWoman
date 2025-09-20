package ru.babaetskv.passionwoman.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProductSize(
    val value: String,
    val isAvailable: Boolean
) : Parcelable {

    companion object {

        val EMPTY = ProductSize("", false)
    }
}
