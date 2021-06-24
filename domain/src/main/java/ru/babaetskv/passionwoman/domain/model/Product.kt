package ru.babaetskv.passionwoman.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Product(
    val id: String,
    val name: String,
    val preview: Image,
    val price: Float,
    val priceWithDiscount: Float, // TODO: handle discount in views
    val rating: Float,
    val colors: List<ProductColor>
) : Parcelable {
    val discountRate: Float
        get() = 100 * (1 - priceWithDiscount / price)
}
