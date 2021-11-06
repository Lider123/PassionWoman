package ru.babaetskv.passionwoman.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Product( // TODO: add description
    val id: String,
    val name: String,
    val preview: Image,
    val price: Float,
    val priceWithDiscount: Float,
    val rating: Float,
    val brand: Brand?,
    val colors: List<ProductColor>
) : Parcelable {
    val discountRate: Float
        get() = 100 * (1 - priceWithDiscount / price)
}
