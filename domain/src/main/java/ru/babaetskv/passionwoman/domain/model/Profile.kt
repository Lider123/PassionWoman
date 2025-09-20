package ru.babaetskv.passionwoman.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Profile(
    val id: Long,
    val name: String,
    val surname: String,
    val phone: String,
    val avatar: Image?
) : Parcelable {
    val isFilled: Boolean
        get() = name.isNotBlank() && surname.isNotBlank()
}
