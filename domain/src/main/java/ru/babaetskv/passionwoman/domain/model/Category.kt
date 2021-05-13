package ru.babaetskv.passionwoman.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Category(
    val id: String,
    val name: String,
    val image: Image
) : Parcelable
