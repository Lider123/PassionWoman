package ru.babaetskv.passionwoman.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Brand(
    val id: Long,
    val logo: Image,
    val name: String
) : Parcelable
