package ru.babaetskv.passionwoman.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import ru.babaetskv.passionwoman.domain.utils.*

@Parcelize
data class Product(
    val id: Int,
    val category: Category,
    val name: String,
    val description: String?,
    val preview: Image,
    val price: Price,
    val priceWithDiscount: Price,
    val rating: Float,
    val brand: Brand?,
    val items: List<ProductItem>
) : Parcelable {
    val discountRate: Float
        get() = (100 * (1 - priceWithDiscount / price)).toFloat()
    val isAvailable: Boolean
        get() = items.any(ProductItem::isAvailable)
}
