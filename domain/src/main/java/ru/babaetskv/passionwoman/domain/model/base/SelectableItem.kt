package ru.babaetskv.passionwoman.domain.model.base

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SelectableItem<T : Parcelable>(
    val value: T,
    val isSelected: Boolean = false
) : Parcelable
