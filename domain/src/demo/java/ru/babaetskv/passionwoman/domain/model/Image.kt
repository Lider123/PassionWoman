package ru.babaetskv.passionwoman.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Image(
    val src: String
) : Parcelable
