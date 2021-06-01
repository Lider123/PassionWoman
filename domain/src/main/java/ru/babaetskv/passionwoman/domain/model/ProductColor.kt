package ru.babaetskv.passionwoman.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProductColor(
    val color: Color,
    val images: List<Image>
) : Parcelable
