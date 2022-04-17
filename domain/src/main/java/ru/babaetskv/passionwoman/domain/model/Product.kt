package ru.babaetskv.passionwoman.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import ru.babaetskv.passionwoman.domain.utils.*

@Parcelize
data class Product(
    val id: String,
    val category: Category,
    val name: String,
    val description: String?,
    val preview: Image,
    val price: Price,
    val priceWithDiscount: Price,
    val rating: Float,
    val brand: Brand?,
    val additionalInfo: Map<String, List<String>>, // TODO: set UI names
    val colors: List<ProductColor>
) : Parcelable {
    val discountRate: Float
        get() = (100 * (1 - priceWithDiscount / price)).toFloat()
    val isAvailable: Boolean
        get() = colors.any(ProductColor::isAvailable)
}
