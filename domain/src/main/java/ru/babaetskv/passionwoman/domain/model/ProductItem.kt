package ru.babaetskv.passionwoman.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProductItem(
    val color: Color,
    val sizes: List<ProductSize>,
    val images: List<Image>
) : Parcelable {
    val isAvailable: Boolean
        get() = sizes.any(ProductSize::isAvailable)
}
