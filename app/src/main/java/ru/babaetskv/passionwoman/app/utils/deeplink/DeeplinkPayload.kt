package ru.babaetskv.passionwoman.app.utils.deeplink

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed class DeeplinkPayload : Parcelable {

    @Parcelize
    data class Product(
        val productId: Int
    ) : DeeplinkPayload()
}
